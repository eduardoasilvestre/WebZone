import java.io.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
public class WebZoneConexao extends MIDlet implements CommandListener,
ItemStateListener, Runnable
{
Display tela;
Form login, resultado;
TextField nome, senha;
StringItem resultadoMsg;
Command sair, proximo;
Thread minhaThread;
public WebZoneConexao()
{
// inicializar Commands
this.sair = new Command("Sair", Command.EXIT, 0);
this.proximo = new Command("Prox", Command.SCREEN, 1);
// form de login
this.login = new Form("Login");
this.nome = new TextField("Nome:", "", 20, TextField.ANY);
this.senha = new TextField("Senha:", "", 20, TextField.ANY |
TextField.PASSWORD);
//adiciona-se os componentes ao Form Login
this.login.append(this.nome);
this.login.append(this.senha);
this.login.addCommand(this.sair);
this.login.addCommand(this.proximo);
this.login.setCommandListener(this);
// form de resultado
this.resultado = new Form("Resultado");
this.resultado.addCommand(this.sair);
this.resultado.setCommandListener(this);
}
public void startApp()
{
this.tela = Display.getDisplay(this);
this.tela.setCurrent(this.login);
}
public void pauseApp()
{
}

public void destroyApp(boolean condicional)
{
}
public void commandAction(Command c, Displayable d)
{
if (c == this.sair) {
this.destroyApp(true);
this.notifyDestroyed();
}
if (c == this.proximo) {
//O Label sempre aparecerá antes do Text não importando
//a ordem que vc adicione ele ao componente
//faça o teste trocando de ordem as chamdas.
    
//    this.tela.setCurrent(this.gauge2);
    this.minhaThread = new Thread(this);
minhaThread.start();
}
}
    public void agenda() {
        
        //carrega foto
        String URL = "http://localhost/web_zone/"+this.senha.getString()+".png";
        InputStream IS = null;
        Image img = null;
        try {
            IS = Connector.openInputStream(URL);
            ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                int dadosEntrada;
                while ((dadosEntrada = IS.read()) != -1)
                    BAOS.write(dadosEntrada);
            byte dadosImagem[] = BAOS.toByteArray();
            img = Image.createImage(dadosImagem, 0, dadosImagem.length);
            if (img != null){
                  ImageItem imagem = new ImageItem("Garota", img, ImageItem.LAYOUT_CENTER, null);
                  int largura = img.getWidth();
                  int altura = img.getHeight();
                  //tela = new Form("Nova Imagem "+largura+"x"+altura );
                  resultado.append(imagem);
             }else
                  resultado = new Form("Erro de download da imagem");
             //display.setCurrent(tela);    
             if(IS != null)
                IS.close();
        } catch (Exception exc) {
            System.out.println("Exception " + exc);
        }
        //Carrega arquivo texto
        String url = "http://localhost/web_zone/"+this.senha.getString()+".txt";
        StreamConnection SC = null;
        IS = null;
        StringBuffer SB = new StringBuffer();
        TextField textbox = null;
        try {
            SC = (StreamConnection)Connector.open(url);
            IS = SC.openInputStream();
            int ch;
            while((ch = IS.read()) != -1) {
                 SB.append((char) ch);
            }
            System.out.println(SB.toString());
            textbox = new TextField(".:: AGENDA  WEB-ZONE ::.", SB.toString(), 1024, 0);
            if(IS != null) {
                  IS.close();
            }
            if(SC != null) {
                 SC.close();
            }
            
            //display.setCurrent(textbox);
            resultado.append(textbox);
            tela.setCurrent(resultado); 
      } catch (Exception exc) {
            System.out.println("Exception " + exc);

         }

      }

    public void itemStateChanged(Item arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

public void run()
{
this.agenda();
}
}