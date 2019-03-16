import java.util.*;

public class Player{

	public enum PlayerType {Healer, Tank, Samurai, BlackMage, Phoenix, Cherry};
	private PlayerType type; 	//Type of this player. Can be one of either Healer, Tank, Samurai, BlackMage, or Phoenix
	private double maxHP;		//Max HP of this player
	private double currentHP;	//Current HP of this player 
	private double atk;			//Attack power of this player
	private int numSpecialTurns;//Num of Special Turns
	private int position;       //Position in team
	private int counter;        //Counter for useSpecialAbility
	private int id;             //Identifier of this play
	private int tauntCounter;   //Counter for tuant
	private int curseCounter;   //Counter for curse
	private int sleepCounter;   //Counter for sleep (Fortune-Cookie)

	private static int auto_increment = 0; // auto increment for id

	/**
	 * Constructor of class Player, which initializes this player's type, maxHP, atk, numSpecialTurns, 
	 * as specified in the given table. It also reset the internal turn count of this player. 
	 * @param _type
	 */
	public Player(PlayerType _type)
	{
		switch(_type)
		{
			case Healer: 	init(_type, 4790, 238, 4);
			case Tank: 		init(_type, 5340, 255, 4);
			case Samurai: 	init(_type, 4005, 368, 3);
			case BlackMage: 	init(_type, 4175, 303, 4);
			case Phoenix: 	init(_type, 4175, 209, 8);
			case Cherry: 	init(_type, 3560, 198, 4);
		}
	}

	/**
	 * initialized Player
	 * @param _type
	 * @param _team
	 * @param _maxHP
	 * @param _atk
	 * @param _numSpecialTurns
	 */
	private void init(PlayerType _type, double _maxHP, double _atk, int _numSpecialTurns)
	{
		this.type = _type;
		this.maxHP = _maxHP;
		this.currentHP = this.maxHP;
		this.atk = _atk;
		this.numSpecialTurns = _numSpecialTurns;
		this.id = ++auto_increment;
		resetCounter();
	}
	
	/**
	 * Returns the current HP of this player
	 * @return
	 */
	public double getCurrentHP()
	{
		return this.currentHP;
	}
	
	/**
	 * Returns type of this player
	 * @return
	 */
	public Player.PlayerType getType()
	{
		return this.type;
	}
	
	/**
	 * Returns max HP of this player. 
	 * @return
	 */
	public double getMaxHP()
	{
		return this.maxHP;
	}
	
	/**
	 * Returns whether this player is sleeping.
	 * @return
	 */
	public boolean isSleeping()
	{
		return this.sleepCounter > 0 ? true : false;
	}
	
	/**
	 * Returns whether this player is being cursed.
	 * @return
	 */
	public boolean isCursed()
	{
		return this.curseCounter > 0 ? true : false;
	}
	
	/**
	 * Returns whether this player is alive (i.e. current HP > 0).
	 * @return
	 */
	public boolean isAlive()
	{
		return this.currentHP > 0 ? true : false;
	}
	
	/**
	 * Returns whether this player is taunting the other team.
	 * @return
	 */
	public boolean isTaunting()
	{
		return this.tauntCounter > 0 ? true : false;
	}

	// set currentHp of this Player
	public void setCurrentHP(double currentHP)
	{
		this.currentHP = currentHP;
	}

	// increase counter by one
	public void increaseCounter()
	{
		this.counter++;
	}

	// reset counter to zero
	public void resetCounter()
	{
		this.counter = 0;
	}

	// get counter of this Player
	public int getCounter()
	{
		return this.counter;
	}

	// set position of this player
	public void setPosition(int position)
	{
		this.position = position;
	}

	// get postion of this player
	public int getPostion()
	{
		return this.position;
	}

	// get identifier
	public int getID()
	{
		return this.id;
	}
	
	/*
		deduce target hp with attack of player
		if target hp less than 0 set it to 0
		hp should not be negative
	*/
	public void attack(Player target)
	{	
		double targetHP = target.getCurrentHP();
		targetHP = targetHP - this.atk;
		targetHP = targetHP < 0 ? 0 : targetHP;
		target.setCurrentHP(targetHP);
	}
	
	public void useSpecialAbility(Player[][] myTeam, Player[][] theirTeam)
	{	
		switch(this.type)
		{
			case Healer: 	heal(myTeam);
			case Tank: 		taunt(theirTeam);
			case Samurai: 	doubleSlash(theirTeam);
			case BlackMage:	curse(theirTeam);
			case Phoenix: 	revive(myTeam);
			case Cherry: 	fortuneCookies(theirTeam);
		}
	}
	
	private void heal(Player[][] myTeam)
	{
		if( this.isCursed() )
		{
			return;
		}

		Player min = getLowestHP(myTeam);
		min.setCurrentHP( min.getCurrentHP() + ( this.getMaxHP() * 0.25 ) );
		if( min.getCurrentHP() > this.getMaxHP() )
		{
			min.setCurrentHP( this.getMaxHP() );
		}
	}

	private void taunt(Player[][] theirTeam)
	{
		Player player = getFirstPosition(theirTeam);
		player.tauntCounter++;
	}
	private void taunt()
	{
		if( this.getCounter() == this.numSpecialTurns )
		{
			if ( this.getType() == PlayerType.Samurai )
			{
				this.doubleSlash(this);
			}
			else if ( this.getType() == PlayerType.BlackMage )
			{
				this.curse(this);
			}
		}
		else
		{
			this.attack(this);
		}
	}

	private void doubleSlash(Player[][] theirTeam)
	{
		Player target = getLowestHP(theirTeam);
		this.doubleSlash(target);
	}
	private void doubleSlash(Player player)
	{
		this.attack(player);
		this.attack(player);
	}

	private void curse(Player[][] theirTeam)
	{
		Player target = getLowestHP(theirTeam);
		curse(target);
	}
	private void curse(Player player)
	{
		// mak sure curse counter always reset
		player.curseCounter = 1;
	}

	private void revive(Player[][] myTeam)
	{
		Player player = getDeadPlayer(myTeam);
		if( player != null )
		{
			player.currentHP = maxHP * 0.3;
			this.resetCounter();
			this.curseCounter = 0;
			this.sleepCounter = 0;
			this.tauntCounter = 0;
		}
	}
	private Player getDeadPlayer(Player[][] team)
	{
		for( Player[] row : team )
		{
			for( Player player : row )
			{
				if( ! player.isAlive() )
				{
					return player;
				}
			}
		}
		return null;
	}

	private void fortuneCookies(Player[][] theirTeam)
	{
		for( Player[] row : theirTeam )
		{
			for( Player player : row )
			{
				player.sleepCounter = 1;
			}
		}
	}
	
	/**
	 * This method is called by Arena when it is this player's turn to take an action. 
	 * By default, the player simply just "attack(target)". However, once this player has 
	 * fought for "numSpecialTurns" rounds, this player must perform "useSpecialAbility(myTeam, theirTeam)"
	 * where each player type performs his own special move. 
	 * @param arena
	 */
	public void takeAction(Arena arena)
	{	
		Player[][] myTeam;
		Player[][] theirTeam;

		// find my team and their team
		if( arena.isMemberOf(this, Arena.Team.A) )
		{
			myTeam = arena.getTeam(Arena.Team.A);
			theirTeam = arena.getTeam(Arena.Team.B);
		}
		else
		{
			myTeam = arena.getTeam(Arena.Team.B);
			theirTeam = arena.getTeam(Arena.Team.A);
		}

		// if curse reduce curse counter
		if( this.isCursed() )
		{
			this.curseCounter--;
		}

		// if tuanting attack or use special to him self
		if( this.isTaunting() )
		{
			this.taunt();
			this.tauntCounter--;
		}
		// if sleep do nothing for this turn
		else if( this.isSleeping() )
		{
			this.sleepCounter--;
			return;
		}
		// can use special?
		else if( this.getCounter() == this.numSpecialTurns )
		{
			// call special ability of sub class
			useSpecialAbility(myTeam, theirTeam);
			this.resetCounter();
		}
		// normal attack
		else
		{
			// attack lowest of their team
			Player lowestTheirTeam = getLowestHP(theirTeam);
			attack(lowestTheirTeam);
			this.increaseCounter();
		}
	}

	// return first position of team
	public Player getFirstPosition(Player[][] team)
	{
		// pull all player to arraylist
		ArrayList<Player> plist = new ArrayList<Player>();
		for(Player[] players : team)
		{
			for( Player player : players )
			{
				plist.add(player);
			}
		}
		
		// and bubble sort by position
		int n = plist.size();
		for(int i=0; i < n; i++)
		{  
			for(int j=1; j < (n-i); j++)
			{
				Player a = plist.get(j-1);
				Player b = plist.get(j);
				
				if( a.getPostion() - b.getPostion() > 0 ){
					//swap elements
					plist.set(j-1, b);
					plist.set(j, a);
				}	
			}
        }
		
		return plist.get(0);
	}

	// return lowest current hp in this team
	public Player getLowestHP(Player[][] team)
	{
		// pull all player to arraylist
		ArrayList<Player> plist = new ArrayList<Player>();
		for(Player[] players : team)
		{
			for( Player player : players )
			{
				plist.add(player);
			}
		}
		
		// and buble sort by hp and index
		int n = plist.size();
		for(int i=0; i < n; i++)
		{  
			for(int j=1; j < (n-i); j++)
			{
				Player a = plist.get(j-1);
				Player b = plist.get(j);
				// compare two player with current hp
				double result = a.getCurrentHP() - b.getCurrentHP();
				// if hp equal then use position instead
				result = result == 0 ? a.getPostion() - b.getPostion() : result;
				if( result > 0 ){
					//swap elements
					Player temp = plist.get(j-1);
					plist.set(j-1, plist.get(j));
					plist.set(j, temp);
				}	
			}
        }
		
		return plist.get(0);
	}
	
	/**
	 * This method overrides the default Object's toString() and is already implemented for you. 
	 */
	@Override
	public String toString()
	{
		return "["+this.type.toString()+" HP:"+this.currentHP+"/"+this.maxHP+" ATK:"+this.atk+"]["
				+((this.isCursed())?"C":"")
				+((this.isTaunting())?"T":"")
				+((this.isSleeping())?"S":"")
				+"]";
	}
}