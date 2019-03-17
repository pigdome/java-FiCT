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
			case Healer: 	init(_type, 4790, 238, 4); break;
			case Tank: 		init(_type, 5340, 255, 4); break;
			case Samurai: 	init(_type, 4005, 368, 3); break;
			case BlackMage: init(_type, 4175, 303, 4); break;
			case Phoenix: 	init(_type, 4175, 209, 8); break;
			case Cherry: 	init(_type, 3560, 198, 4); break;
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
		if( target == null )
		{
			System.out.println("call method attack with null player");
			return;
		}
		System.out.println("[" + this.position + "] {" + this.type + "} attack [" + target.position + "] {" + target.type + "}");
		double targetHP = target.getCurrentHP();
		targetHP = targetHP - this.atk;
		targetHP = targetHP < 0 ? 0 : targetHP;
		target.setCurrentHP(targetHP);
	}
	
	public void useSpecialAbility(Player[][] myTeam, Player[][] theirTeam)
	{	
		switch(this.type)
		{
			case Healer: 	heal(myTeam); break;
			case Tank: 		taunt(); break;
			case Samurai: 	doubleSlash(theirTeam); break;
			case BlackMage:	curse(theirTeam); break;
			case Phoenix: 	revive(myTeam); break;
			case Cherry: 	fortuneCookies(theirTeam); break;
		}
	}
	
	private void heal(Player[][] myTeam)
	{
		if( this.isCursed() )
		{
			return;
		}

		Player min = getLowestHP(myTeam, false);
		min.setCurrentHP( min.getCurrentHP() + ( min.getMaxHP() * 0.25 ) );
		if( min.getCurrentHP() > min.getMaxHP() )
		{
			min.setCurrentHP( min.getMaxHP() );
		}
	}

	private void taunt()
	{
		this.tauntCounter++;
	}

	private void doubleSlash(Player[][] theirTeam)
	{
		Player target = getLowestHP(theirTeam, true);
		this.doubleSlash(target);
	}
	private void doubleSlash(Player player)
	{
		this.attack(player);
		this.attack(player);
	}

	private void curse(Player[][] theirTeam)
	{
		Player target = getLowestHP(theirTeam, false);
		if( target != null )
		{
			curse(target);
		}
	}
	private void curse(Player player)
	{
		// mak sure curse counter always reset
		System.out.println( player.type + "@" + player.position + " is curse");
		player.curseCounter = 2;
	}

	private void revive(Player[][] myTeam)
	{
		Player player = getDeadPlayer(myTeam);
		if( player != null )
		{
			player.currentHP = player.maxHP * 0.3;
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
				System.out.println( player.type + "@" +player.position+" eat fortune cookies by" + this.type+"@"+this.position);
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
		//System.out.println( this.type + "@" +this.position+" take action");
		Player[][] myTeam;
		Player[][] theirTeam;
		
		// if curse reduce curse counter
		if( this.isCursed() )
		{
			this.curseCounter--;
		}

		// if taunting reduce taunt counter
		if( this.isTaunting() )
		{
			this.tauntCounter--;
		}

		// if dead do nothing
		if( ! this.isAlive() )
		{
			this.increaseCounter();
			return;
		}

		// if sleep do nothing for this turn
		if( this.isSleeping() )
		{
			this.sleepCounter--;
			System.out.println( this.type + "@" +this.position+" sleep");
			return;
		}

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
		
		
		// can use special?
		this.increaseCounter();
		if( this.getCounter() == this.numSpecialTurns )
		{
			// call special ability of sub class
			System.out.println( this.type + "@" +this.position+" use special");
			useSpecialAbility(myTeam, theirTeam);
			this.resetCounter();
		}
		// normal attack
		else
		{
			// attack lowest of their team
			Player lowestTheirTeam = getLowestHP(theirTeam, true);
			attack(lowestTheirTeam);
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
	public Player getLowestHP(Player[][] team, boolean forAttack)
	{
		Player lowest;

		// return taunting player first
		if( forAttack )
		{
			lowest = getTaunting(team);
			if( lowest != null )
			{
				return lowest;
			}
		}
		
		// here return some one
		lowest = getLowestHP(team[0]);
		if( lowest != null )
		{
			return lowest;
		}
		else
		{
			return getLowestHP(team[1]);
		}

		//return lowest;
	}
	private Player getTaunting(Player[][] team)
	{
		ArrayList<Player> plistTaunt = new ArrayList<Player>();
		
		for(Player[] row : team)
		{
			for(Player player : row)
			{
				if( player.isAlive() && player.isTaunting() )
				{
					plistTaunt.add(player);
				}
			}
		}
		
		int n = plistTaunt.size();
		// sort by position
		for(int i=0; i < n; i++)
		{  
			for(int j=1; j < (n-i); j++)
			{
				Player a = plistTaunt.get(j-1);
				Player b = plistTaunt.get(j);
				
				if( a.getPostion() - b.getPostion() > 0 ){
					//swap elements
					plistTaunt.set(j-1, b);
					plistTaunt.set(j, a);
				}	
			}
		}
			
		return plistTaunt.size() > 0 ? plistTaunt.get(0) : null;
	}
	private Player getLowestHP(Player[] team)
	{
		// pull all player to arraylist
		ArrayList<Player> plist = new ArrayList<Player>();
		

		for(Player player : team)
		{
			if( player.isAlive() )
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
					plist.set(j-1, b);
					plist.set(j, a);
				}	
			}
        }
		//System.out.println("lowest hp is " + this.type + ", "+ this.position);
		return plist.size() > 0 ? plist.get(0) : null;
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