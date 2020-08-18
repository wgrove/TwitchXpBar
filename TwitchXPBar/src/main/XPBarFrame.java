package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

public class XPBarFrame extends Canvas {

	private static final long serialVersionUID = 2417354717211422071L;

	private static final String lvlText = "LVL: ";
	private static final String lvlUpText = " LEVEL UP ";
	private static final String xpText = "XP:";
	private static final String nxtLvlText = "Next Level In: ";

	private Color frameColor;
	private Color textColor;
	private Color xpBarColor;
	private Color xpPlusColor;
	private Color xpMinusColor;
	private Color levelUpColor;
	private XpManager xpManager;
	private boolean showXpChange = false;
	private boolean showLevelUp = false;

	public XPBarFrame(Color frameColor, Color textColor, Color xpBarColor, Color xpPlusColor, Color xpMinusColor, Color levelUpColor) {
		super();
		this.frameColor = frameColor;
		this.textColor = textColor;
		this.xpBarColor = xpBarColor;
		this.xpPlusColor = xpPlusColor;
		this.xpMinusColor = xpMinusColor;
		this.levelUpColor = levelUpColor;
		this.xpManager = new XpManager();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		int x[] = {0, 0, 250, 250, 90, 65};
		int y[] = {0, 50, 50, 18, 18, 0};

		g.setColor(frameColor);
		g.fillPolygon(x, y, 6);
		g.setColor(textColor);

		Font tr = new Font("Arial", Font.BOLD, 16);
		g.setFont(tr);
		g.drawString(lvlText + xpManager.getLevel(), 3, 20);

		tr = new Font("Arial", Font.BOLD, 12);
		g.setFont(tr);
		g.drawString(xpText, 5, 45);

		tr = new Font("Arial", Font.BOLD, 10);
		g.setFont(tr);
		g.drawString(nxtLvlText + xpManager.getRemainingXp(), 90, 35);

		g.fillRect(25, 37, 220, 8);

		g.setColor(xpBarColor);

		int xpBarWidth = (int) Math.floor(xpManager.getLevelCompletionPercentage() * 220);

		g.fillRect(25, 37, xpBarWidth, 8);

		if(showXpChange) {

			StringBuilder builder = new StringBuilder();

			if(xpManager.getXpChange() > 0) {
				builder.append("+");
				g.setColor(xpPlusColor);
			} else {
				g.setColor(xpMinusColor);
			}

			builder.append(xpManager.getXpChange());

			tr = new Font("Arial", Font.BOLD, 20);
			g.setFont(tr);
			g.drawString(builder.toString(), 90, 16);

			if(showLevelUp) {
				int stringWidth = g.getFontMetrics().stringWidth(builder.toString());
				int lvlUpStringWidth = g.getFontMetrics().stringWidth(lvlUpText);

				int desiredWidth = stringWidth + lvlUpStringWidth + 5;

				int x2[] = {100 + desiredWidth, 92 + desiredWidth, 97 + desiredWidth, 97 + desiredWidth, 103 + desiredWidth, 103 + desiredWidth, 108 + desiredWidth};
				int y2[] = {0, 8, 8, 16, 16, 8, 8};
				g.setColor(levelUpColor);
				g.drawString(lvlUpText, 90 + stringWidth + 10, 16);
				g.fillPolygon(x2, y2, 7);
			}

		}

		;

	}

	public void updateXp(Integer xp) {
		int status = xpManager.setXp(xp);
		repaint();
		showXpChange(status);
	}

	public void showXpChange(int status) {

		if(status >= 0) {

			Timer timer = new Timer();
			showXpChange = true;

			if(status == 1) {
				showLevelUp = true;
			}
			
			safeRepaint();

			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					showXpChange = false;
					showLevelUp = false;
					timer.cancel();
				}
			}, 3000);

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			safeRepaint();

		}
	}

	private void safeRepaint() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				repaint();
			}
		});
	}
}
