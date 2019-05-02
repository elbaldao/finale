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
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
				  .apiKey("Lbe4BBfVpfut4CaPy-zGLIhlW1LPZwV1wwed4fn17qi-")
				  .build();
				service.setIamCredentials(options);
				
				service.setEndPoint("https://gateway-lon.watsonplatform.net/text-to-speech/api");

				String text = "Hello mama";
				
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
				File dir = new File("C:\\Users\\loren\\Desktop\\texts");
				dir.mkdir();
		//ruta dove inviare C:\\Users\\loren\\asrTomcatEjemploCloudant-master\\src\\main\\java\\asr\\proyectoFinal\\canciones
				
				//Date dat= new Date();
				String data_corr=text.substring(0, 5);
				//String data_corr= dat.getDate()+ "/" + dat.getMonth() + "/" + dat.getYear()+"_"+dat.getHours()+"-"+dat.getMinutes()+"-"+dat.getSeconds();
				String name= data_corr+".wav";
				File filename = new File(dir, name);
				OutputStream fileOutputStream = new FileOutputStream(filename);			
				byteArrayOutputStream.writeTo(fileOutputStream);
				out.println("SCARICATO");
				
		/*		try {
			        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("C:\\Users\\loren\\Desktop\\texts"+name).getAbsoluteFile());
			        Clip clip = AudioSystem.getClip();
			        clip.open(audioInputStream);
			        clip.start();
			        out.println("Sto leggendo");
			    } catch(Exception ex) {
			        out.println("Error with playing sound.");
			        ex.printStackTrace();
			    }
		*/		
				

				// clean up
				byteArrayOutputStream.close();
				
				fileOutputStream.close();	
				
				//out.println("LISTO");
				
				
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
