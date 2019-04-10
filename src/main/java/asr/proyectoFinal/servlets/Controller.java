package asr.proyectoFinal.servlets;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voices;
import com.ibm.watson.developer_cloud.text_to_speech.v1.websocket.BaseSynthesizeCallback;

import asr.proyectoFinal.dao.CloudantPalabraStore;
import asr.proyectoFinal.dominio.Palabra;

/**
 * Servlet implementation class Controller
 */
@WebServlet(urlPatterns = {"/listar", "/insertar", "/hablar", "/texttospeech"})
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		PrintWriter out = response.getWriter();
		out.println("<html><head><meta charset=\"UTF-8\"></head><body>");
		
		CloudantPalabraStore store = new CloudantPalabraStore();
		System.out.println(request.getServletPath());
		switch(request.getServletPath())
		{
			case "/listar":
				if(store.getDB() == null)
					  out.println("No hay DB");
				else
					out.println("Palabras en la BD Cloudant JUVE:<br />" + store.getAll());
				break;
				
				
//-------------------------------------------------------------------------------------------------------		
			
			case "/texttospeech":				
				
				TextToSpeech service = new TextToSpeech();
				IamOptions options = new IamOptions.Builder()
				  .apiKey("Bc2tMUhywg6IcCgctVY6b_hSTQmMhYBTh8djshN6-VEG")
				  .build();
				service.setIamCredentials(options);
				
				service.setEndPoint("https://gateway-lon.watsonplatform.net/text-to-speech/api");

				String text = request.getParameter("letra");
				
				SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder()
				  .text(text)
				  .accept(SynthesizeOptions.Accept.AUDIO_OGG_CODECS_OPUS)
				  .build();
				// a callback is defined to handle certain events, like an audio transmission or a timing marker
				// in this case, we'll build up a byte array of all the received bytes to build the resulting file
				final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				service.synthesizeUsingWebSocket(synthesizeOptions, new BaseSynthesizeCallback() {
				  @Override
				  public void onAudioStream(byte[] bytes) {
				    // append to our byte array
				    try {
				      byteArrayOutputStream.write(bytes);
				    } catch (IOException e) {
				      e.printStackTrace();
				    }
				  }
				});

				// quick way to wait for synthesis to complete, since synthesizeUsingWebSocket() runs asynchronously
			
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				// create file with audio data
				File dir = new File("C:\\Users\\loren\\asrTomcatEjemploCloudant-master\\src\\main\\java\\asr\\proyectoFinal\\canciones");
				dir.mkdir();
			
				String name= request.getParameter("nombre")+".mp3";
				
				File filename = new File(dir, name);
				OutputStream fileOutputStream = new FileOutputStream(filename);			
				byteArrayOutputStream.writeTo(fileOutputStream);
				

				// clean up
				byteArrayOutputStream.close();
				fileOutputStream.close();	
				
				out.println("LISTO");
				
				
				break;
				
//------------------------------------------------------------------------------------------------
				
				
			case "/insertar":
				Palabra palabra = new Palabra();
				String parametro = request.getParameter("palabra");

				if(parametro==null)
				{
					out.println("usage: /insertar?palabra=palabra_a_traducir");
				}
				else
				{
					if(store.getDB() == null) 
					{
						out.println(String.format("Palabra: %s", palabra));
					}
					else
					{
						palabra.setName(parametro);
						store.persist(palabra);
					    out.println(String.format("Almacenada la palabra: %s", palabra.getName()));			    	  
					}
				}
				break;
		}
		out.println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
