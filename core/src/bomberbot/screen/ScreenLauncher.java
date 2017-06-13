package bomberbot.screen;

import bomberbot.Globals;
import bomberbot.button.ButtonBasic;
import bomberbot.main.Main;
import bomberbot.net.Connexion;
import bomberbot.net.MD5;
import bomberbot.net.User;
import jdk.nashorn.internal.objects.Global;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.sun.jndi.toolkit.url.Uri;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by sio1 on 02/03/2016.
 */
public class ScreenLauncher extends ScreenBasic
{
    public BitmapFont font = new BitmapFont();
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    private Stage stage;
    private TextField usernameField;
    private TextField passField;
    private ButtonBasic buttonConnexion;
    private ButtonBasic buttonURL;
    private User user;

    public ScreenLauncher(Main game) {

        super(game);

        stage = new Stage();

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        ScreenIngame.font.getData().setScale(6);
        this.buttonConnexion = new ButtonBasic(Globals.V_WIDTH/2 - 280, Globals.V_HEIGHT/2 - 400, "Connection");
        this.buttonConnexion.setWidth(1000);
        this.buttonConnexion.setHeight(300);
        this.buttonURL = new ButtonBasic(Globals.V_WIDTH - 150, Globals.V_HEIGHT - 150, "Sign up");
        this.buttonURL.setWidth(300);
        this.buttonURL.setHeight(150);
        Skin head = new Skin(Gdx.files.internal("defaultskin.json"));
        TextField.TextFieldStyle textFieldStyle = head.get(TextField.TextFieldStyle.class);
        textFieldStyle.font.getData().setScale(4);

        usernameField = new TextField("Username", head);
        usernameField.setPosition(Globals.V_WIDTH / 2 - 150, Globals.V_HEIGHT / 2);
        usernameField.setSize(500, 120);

        passField = new TextField("Password", head);
        passField.setPosition(Globals.V_WIDTH / 2 - 150, Globals.V_HEIGHT / 2 - 60);
        passField.setSize(500, 120);
        passField.setPasswordCharacter('*');
        passField.setPasswordMode(true);


        table.add(usernameField).size(usernameField.getWidth(), usernameField.getHeight()).padTop(10).padBottom(10);
        table.row();
        table.add(passField).size(passField.getWidth(), passField.getHeight()).padBottom(150);
        table.row();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void update(float delta)
    {
        super.update(delta);
        if(buttonURL.onClick(gamePort))
        {
            Gdx.net.openURI("http://www.bomberbot.comxa.com/inscription.php");
        }
        if(buttonConnexion.onClick(gamePort))
        {
            Thread thread = new Thread(new Runnable()
            {

                public void run()
                {
                String htmlsrc = null;
                try
                {
                    htmlsrc = Connexion.doHttpUrlConnectionAction(usernameField.getText(), MD5.encode(passField.getText()));
                    System.out.println(htmlsrc);
                    if(htmlsrc.contains("true"))
                    {
                        Globals.user = new User(usernameField.getText());
                        game.setScreen(new ScreenMenu(game));
                    } else
                    {
                        passField.setText("");
                    }
                } catch (Exception e)
                {

                    e.printStackTrace();
                }
                }
            });
            thread.start();
        }
    }

    @Override
    public void render(float delta)
    {
        super.render(delta);
        shapeRenderer.setProjectionMatrix(Globals.camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.2f, 0.7f, 0.8f, 1));
        shapeRenderer.rect(0, 0, Globals.camera.viewportWidth, Globals.camera.viewportHeight);
        shapeRenderer.end();
        stage.draw();
        Globals.batch.begin();
        this.buttonConnexion.draw();
        this.buttonURL.draw();
        Globals.batch.end();
    }
}
