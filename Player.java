import java.util.*;

public class Player {

	public enum PlayerType {Healer, Tank, Samurai, BlackMage, Phoenix, Cherry};
	private enum PlayerStatus {Sleeping, Taunting, Cursed};

	private PlayerStatus status;//Status of this player. Can be one of either Sleeping, Taunting, Cursed or NULL ( for normal case )
	private PlayerType type; 	//Type of this player. Can be one of either Healer, Tank, Samurai, BlackMage, or Phoenix
	private double maxHP;		//Max HP of this player
	private double currentHP;	//Current HP of this player 
	private double atk;			//Attack power of this player
	private int numSpecialTurns;//Num of Special Turns
	private int position;       //Position in team
	private int counter;        //Counter for useSpecialAbility

	/**
	 * Constructor of class Player, which initializes this player's type, maxHP, atk, numSpecialTurns, 
	 * as specified in the given table. It also reset the internal turn count of this player. 
	 * @param _type
	 * @param _maxHP
	 * @param _atk
	 * @param _numSpecialTurns
	 */
	public Player(PlayerType _type, double _maxHP, double _atk, int _numSpecialTurns)
	{
		this.type = _type;
		this.maxHP = _maxHP;
		this.atk = _atk;
		this.numSpecialTurns = _numSpecialTurns;
		ResetCounter();
	}
	
	/**
	 * Returns the current HP of this player
	 * @return
	 */
	public double getCurrentHP()
	{
		//INSERT YOUR CODE HERE
		return 0;
	}
	
	/**
	 * Returns type of this player
	 * @return
	 */
	public Player.PlayerType getType()
	{
		//INSERT YOUR CODE HERE
		return null;
	}
	
	/**
	 * Returns max HP of this player. 
	 * @return
	 */
	public double getMaxHP()
	{
		//INSERT YOUR CODE HERE
		
		return 0;
	}
	
	/**
	 * Returns whether this player is sleeping.
	 * @return
	 */
	public boolean isSleeping()
	{
		//INSERT YOUR CODE HERE
		
		return false;
	}
	
	/**
	 * Returns whether this player is being cursed.
	 * @return
	 */
	public boolean isCursed()
	{
		//INSERT YOUR CODE HERE
		
		return false;
	}
	
	/**
	 * Returns whether this player is alive (i.e. current HP > 0).
	 * @return
	 */
	public boolean isAlive()
	{
		//INSERT YOUR CODE HERE
		
		return true;
	}
	
	/**
	 * Returns whether this player is taunting the other team.
	 * @return
	 */
	public boolean isTaunting()
	{
		//INSERT YOUR CODE HERE
		return false;
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
	
	
	public void attack(Player target)
	{	
		//INSERT YOUR CODE HERE
	}
	
	public void useSpecialAbility(Player[][] myTeam, Player[][] theirTeam)
	{	
		//INSERT YOUR CODE HERE
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
		//INSERT YOUR CODE HERE
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

public class SortByHpAndIndex implements Comparator<Player>
{
	@override
	public int compare(Player a, Player b)
	{
		int result;
		result = a.getCurrentHP() - b.getCurrentHP();
		if( result == 0 )
		{
			result = a.getPostion() - b.getPostion();
		}
		return result;
	}
}

public class Healer extends Player
{
	public Healer()
	{
		super(Player.PlayerType.Healer, 4790, 238, 4);
	}
	public void useSpecialAbility(Player[][] myTeam, Player[][] theirTeam)
	{
		// pull all player to arraylist
		// and sort by hp and index
		ArrayList<Player> plist = new ArrayList<Player>();
		for(Player[] players : myTeam)
		{
			for( Player player : players )
			{
				plist.add(player);
			}
		}
		Collections.sort(plist, new SortByHpAndIndex());
		Player min = plist.get(0);

		// heal lowest player
		min.setCurrentHP( min.getCurrentHP() + ( this.getMaxHP() * 0.25 ) );
		if( min.getCurrentHP() > this.getMaxHP() )
		{
			min.getCurrentHP( this.getMaxHP() );
		}
	}
}