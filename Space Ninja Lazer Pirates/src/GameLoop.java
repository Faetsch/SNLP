


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class GameLoop extends AnimationTimer
{
	public int player = 0; //0 = left, 1 = right
    private GameCanvas canvas;
    private GraphicsContext gc;
    private Soldier[][] soldiers = new Soldier[2][3];
    private Soldier selectedSoldier;
    private Sprite background = null;
    private Image[] imgLeftFlashlightSoldierIdle = new Image[20];
    private Image[] imgLeftRifleSoldierIdle = new Image[20];
    private Image[] imgLeftPistolSoldierIdle = new Image[20];
    private Image[] imgRightFlashlightSoldierIdle = new Image[20];
    private Image[] imgRightRifleSoldierIdle = new Image[20];
    private Image[] imgRightPistolSoldierIdle = new Image[20];
    private ArrayList<Missile> missiles = new ArrayList<Missile>();
    private Image[] imgHpBars = new Image[3];
    private Image imgBackground = null;
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;
    private Socket server;
    private GameState lastGameState = new GameState();


    public GameLoop(GameCanvas canvas)
    {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        try
        {
        	this.server = new Socket("localhost", 3000); //laptop lenovo
        	oos = new ObjectOutputStream(server.getOutputStream());
        	ois = new ObjectInputStream(server.getInputStream());
        	player = ois.readInt();
        	updateGame();
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }
        initSprites();

    }

    public void handle(long currentNanoTime)
    {


        //rendering

//    	System.out.println("client " + lastGameState.absoluteTime);
        gc.clearRect(0, 0, 1000, 600);
        background.render(gc);
        for(Soldier s : soldiers[0])
        {
        	//s.update(lastGameState.elapsedTime);
            if(s.getHp() > 0)
            {
                gc.drawImage(imgHpBars[s.getHp()-1], s.getPositionX()-imgHpBars[0].getWidth(), s.getPositionY());
                s.render(gc, lastGameState.absoluteTime);
                if(s == selectedSoldier)
                {
            		gc.setStroke(Color.BLUE);
            		gc.strokeRect(s.getPositionX(), s.getPositionY(), s.getFrame(lastGameState.elapsedTime).getWidth(), s.getFrame(lastGameState.elapsedTime).getHeight());
                }
            }

        }

        for(Soldier s : soldiers[1])
        {
        	//s.update(lastGameState.elapsedTime);
            if(s.getHp() > 0)
            {
                gc.drawImage(imgHpBars[s.getHp()-1], s.getPositionX() + imgHpBars[0].getWidth() + s.getWidth()-20, s.getPositionY());
                s.render(gc, lastGameState.absoluteTime);
                if(s == selectedSoldier)
                {
            		gc.setStroke(Color.ORANGE);
            		gc.strokeRect(s.getPositionX(), s.getPositionY(), s.getFrame(lastGameState.elapsedTime).getWidth(), s.getFrame(lastGameState.elapsedTime).getHeight());
                }
            }
        }


        Iterator<Missile> iterMissile = missiles.iterator();
        while(iterMissile.hasNext())
        {
            Missile m = iterMissile.next();
            if(m.left)
            {
            	m.setImage("/missileLeft.png");
            }
            else
            {
            	m.setImage("/missileRight.png");
            }
    		m.render(gc);
//            for(Soldier s : soldiers[0])
//            {
//                if(s.intersects(m) && s.getHp() > 0)
//                {
//                    iterMissile.remove();
//                }
//                else
//                {
//                    m.render(gc);
//                }
//            }
//
//            for(Soldier s : soldiers[1])
//            {
//                if(s.intersects(m) && s.getHp() > 0)
//                {
//                    iterMissile.remove();
//                }
//                else
//                {
//                    m.render(gc);
//                }
//             }
         }
    }

    private void initSprites()
    {
        for(int i = 0; i < 3; i++)
        {
            imgHpBars[i] = new Image("/hpbar/" + Integer.valueOf(i+1) + "hpbar.png", 20, 75, false, false);
        }

        for(int i = 0; i < 20; i++)
        {
            imgLeftFlashlightSoldierIdle[i] = new Image("/leftsidefigures/flashlight/idle/survivor-idle_flashlight_" + i + ".png", 120, 75, false, false);
            imgLeftRifleSoldierIdle[i] = new Image("/leftsidefigures/rifle/idle/survivor-idle_rifle_" + i + ".png", 120, 75, false, false);
            imgLeftPistolSoldierIdle[i] = new Image("/leftsidefigures/handgun/idle/survivor-idle_handgun_" + i + ".png", 120, 75, false, false);
            imgRightFlashlightSoldierIdle[i] = new Image("/leftsidefigures/flashlight/idle/survivor-idle_flashlight_" + i + ".png", 120, 75, false, false);
            imgRightRifleSoldierIdle[i] = new Image("/leftsidefigures/rifle/idle/survivor-idle_rifle_" + i + ".png", 120, 75, false, false);
            imgRightPistolSoldierIdle[i] = new Image("/leftsidefigures/handgun/idle/survivor-idle_handgun_" + i + ".png", 120, 75, false, false);
        }


        soldiers[0][0].setFrames(imgLeftFlashlightSoldierIdle);
        soldiers[0][1].setFrames(imgLeftRifleSoldierIdle);
        soldiers[0][2].setFrames(imgLeftPistolSoldierIdle);
        soldiers[1][0].setFrames(imgRightFlashlightSoldierIdle);
        soldiers[1][1].setFrames(imgRightRifleSoldierIdle);
        soldiers[1][2].setFrames(imgRightPistolSoldierIdle);
        imgBackground = new Image("/space.png");
        background = new Sprite();
        background.setImage(imgBackground);
        background.setPosition(0, 0);
    }

    private void updateGame()
    {
    	new Thread()
    	{
    		public void run()
    		{
    			while(true)
    			{
    	    		try
    	    		{
    	    			Thread.sleep(8);
    	    			lastGameState = (GameState) ois.readUnshared();
    	    			lastGameState.setSelectedSoldier(player, ois.readInt());
    	    			lastGameState.wonBy = ois.readInt();
    	    			if(lastGameState.wonBy != 2)
    	    			{
    	    				System.out.println(lastGameState.wonBy + " won");
    	    			}
    	    			for(int i = 0; i < 3; i++)
    	    			{
    	    				for(int k = 0; k < 2; k++)
    	    				{
    	    					lastGameState.getSoldiers()[k][i].setPositionX(ois.readDouble());
    	    					lastGameState.getSoldiers()[k][i].setPositionY(ois.readDouble());
    	    					lastGameState.getSoldiers()[k][i].setHp(ois.readInt());
    	    				}
    	    			}

    	    			lastGameState.missiles.clear();
    	    			int numberMissiles = ois.readInt();
    	    			for(int i = 0; i < numberMissiles; i++)
    	    			{
    	    				double x = ois.readDouble();
    	    				double y = ois.readDouble();
    	    				boolean left = ois.readBoolean();
    	    				lastGameState.missiles.add(new Missile(x, y,  left));
    	    			}



//    	    			System.out.println(lastGameState);
//    	    			System.out.println("absolute Time: " + lastGameState.absoluteTime);
//    	    			System.out.println("elapsed Time: " + lastGameState.elapsedTime);
//    	    			System.out.println("Player " + player + " at : (" + lastGameState.getSoldiers()[player][0].getPositionX() + "|" + lastGameState.getSoldiers()[player][0].getPositionY() + ")");
    	    			soldiers = lastGameState.getSoldiers();
    	    	//		System.out.println("client : " + lastGameState.elapsedTime);
    	    			missiles = lastGameState.getMissiles();
    	    		//	System.out.println("client selected " + lastGameState.getSelectedSoldier(player));
    	    			selectedSoldier = lastGameState.getSoldiers()[player][lastGameState.getSelectedSoldier(player)];

    	    		}

    	    		catch (ClassNotFoundException e)
    	    		{
    	    			e.printStackTrace();
    	    		}

    	    		catch (IOException e)
    	    		{
    	    			e.printStackTrace();
    	    		}
    	    		catch (InterruptedException e)
    	    		{
						e.printStackTrace();
					}

    				try
    				{
    					Thread.sleep(8);
    					ArrayList<String> inputs = (ArrayList<String>)SNLP.inputs.clone();
//    					ArrayList<String> testList = new ArrayList<String>();
//    					testList.add("RIGHT");
//    					testList.add("DOWN");
    					oos.writeUnshared(inputs);
    					oos.flush();
    					//System.out.println("Client flushed inputs");
    				}

    				catch (IOException | InterruptedException e)
    				{
    					e.printStackTrace();
    				}

    			}
    		}
    	}.start();

//    	new Thread()
//    	{
//    		public void run()
//    		{
//				try
//				{
//					Thread.sleep(8);
//					ArrayList<String> testList = (ArrayList<String>)SNLP.inputs.clone();
////					ArrayList<String> testList = new ArrayList<String>();
////					testList.add("RIGHT");
////					testList.add("DOWN");
//					oos.writeObject(testList);
//					oos.flush();
//					//System.out.println("Client flushed inputs");
//				}
//
//				catch (IOException | InterruptedException e)
//				{
//					e.printStackTrace();
//				}
//    		}
//    	}.start();
    }


}
