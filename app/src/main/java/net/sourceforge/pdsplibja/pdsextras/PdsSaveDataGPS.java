/*
 * Copyright (c) 2015. Fernando Pujaico Rivera <fernando.pujaico.rivera@gmail.com>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.sourceforge.pdsplibja.pdsextras;


import java.io.*;
import android.content.Context;
import android.widget.Toast;
import android.os.Environment;

/**
 * Esta classe implementa uma forma de salvar dados (4 valores )
 * na memoria SD, usando bibliotecas de Android,
 * <br>
 * De forma geral a ideia é usar esta função para uma variável
 * de tempo e 3 de dimensão.
 * No arquivo são vistas 4 colunas de informação representando
 * esses 4 valores.
 * <br>
 * Esta classe escrive os dados em:/storage/sdcard0/Android/rota.da.classe/
 * <br>
 * Por isso precisa ser entregada à classe uma variável de tipo Context.
 * <br>
 * É necesario :<br>
 *     &lt;uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"&gt; &lt;/uses-permission&gt;
 * <br>
 * En AndroidManifest.xml
 *
 * @author Fernando Pujaico Rivera <a href="mailto:fernando.pujaico.rivera@gmail.com">fernando.pujaico.rivera@gmail.com</a>
 * @version 0.01
 * @since 2015-05-25
 * @see <a href="http://pdsplib.sourceforge.net"> PDS Project Libraries in Java for Android </a>
 */
public class PdsSaveDataGPS {
	private String FileName;
	private Context ctx;
	private boolean ERRO=false;
	private boolean ERROWRITE=false;
	private boolean cerrado=true;

	File ruta_sd;
	File f;
	OutputStreamWriter fout;
	/**
	 * Este método é o construtor da classe.
	 * <br>
	 * É necessário inicializar esta classe indicando a rota e nome do arquivo.
	 *
	 * @param context É o contexto da aplicação. Este é obtido desde a classe principal com
	 *                getApplicationContext(). Esta função só pode ser usado na classe principal.
	 *                No caso contrario tem que ser usado algumcontext.getApplicationContext(),
	 *                sendo algumcontext uma instancia da classe Context.
	 * @param filename Nome do arquivo a ser criado.
	 **/
	public PdsSaveDataGPS(Context context, String filename) {
		this.FileName=filename;
		this.ctx=context;

		//Comprovamos o estado de la memoria externa (tarjeta SD)
		String estado = Environment.getExternalStorageState();
		if (estado.equals(Environment.MEDIA_MOUNTED))
		{
			this.ruta_sd 	= this.ctx.getExternalFilesDir(null);

			try {
				this.f 			= new File(ruta_sd.getAbsolutePath(), this.FileName);
				this.fout		= new OutputStreamWriter(new FileOutputStream(f));
				this.ERRO=false;
				this.cerrado=false;
			}
			catch (Exception e) {
				this.ERRO=true;
				e.printStackTrace();

				Toast toast = Toast.makeText(this.ctx,
						"No puedo iniciar la escritura de:\n"+this.f.getAbsolutePath()+"/"+this.FileName,
						Toast.LENGTH_LONG);
				toast.show();
			}
		}
		else
		{
			this.ERRO=true;
			Toast toast = Toast.makeText(this.ctx,
					"No puedo escribir en la SDCARD",
					Toast.LENGTH_LONG);
			toast.show();
		}


	}
	
	/**
	 * Este método escreve no formato ASCII 3 dados no arquivo. 
	 * <br>
	 * Estes dados estão escritos numa linha separados por um TAB e 
	 * com um salto de linha no final.
	 *
	 * @param T Tempo da tomada de dados.
	 * @param X Primeiro dado a ser escrito no arquivo.
	 * @param Y Segundo dado a ser escrito no arquivo.
	 * @param Z Terceiro dado a ser escrito no arquivo.
	 **/
	public void Printf(long ID, String Texto) {
		String cadena1;
		
		if((this.ERRO!=true)&&(this.cerrado==false)){
			cadena1=ID + "\t" + Texto + "\n";

			try {
				this.fout.write(cadena1);
				this.ERROWRITE=false;
			}
			catch (IOException e) {
				e.printStackTrace();
				this.ERROWRITE=true;
				Toast toast = Toast.makeText(this.ctx,
											"No puedo escribir en:\n"+this.f.getAbsolutePath()+"/"+this.FileName,
											Toast.LENGTH_SHORT);
				toast.show();
			}
			//this.outputStream.close();
		}
	}


	/**
	 * Close the file 
	 **/
	public void close() {
		try {
			this.fout.close();
			this.cerrado=true;
		} catch (IOException e) {
			e.printStackTrace();
			Toast toast = Toast.makeText(this.ctx,
					"Tive problemas ao fechar:\n"+this.f.getAbsolutePath()+"/"+this.FileName,
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * Este método retorna o endereço completo do arquivo.
	 *
	 * @return Este método retorna o endereço completo do arquivo.
	 * */	
	public String GetFileName(){
		if(this.ERRO!=true){
			return this.f.getAbsolutePath();
		}
		else{
			return this.f.getAbsolutePath()+"/"+this.FileName;
		}
	}

	/**
	 * Este método retorna true se todo foi bem na escrita de data ou false se não.
	 *
	 * @return Este método retorna true se todo foi bem na escrita de data ou false se não.
	 * */
	public boolean GetWriteErro(){
		return this.ERROWRITE;
	}

	/**
	 * Este método retorna true se todo foi criando o arquivo ou false se não.
	 *
	 * @return Este me'todo retorna true se todo foi criando o arquivo ou false se não.
	 * */
	public boolean GetCreateErro(){
		return this.ERRO;
	}
	
	/**
	 * Este método provoca que se usamos uma instancia de PdsSaveDataGPS
	 * num contexto que necessita-se ser String, então esta instancia
	 * retorna o endereço completo do arquivo.
	 * */
	public String toString() {
		if(this.ERRO!=true){
			return this.f.getAbsolutePath();
		}
		else{
			return this.FileName;
		}
	}
}
