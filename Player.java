import java.util.*;

public class Player {

	public enum PlayerType {Healer, Tank, Samurai, BlackMage, Phoenix, Cherry};
	
	private PlayerType type; 	//Type of this player. Can be one of either Healer, Tank, Samurai, BlackMage, or Phoenix
	private double maxHP;		//Max HP of this player
	private double currentHP;	//Current HP of this player 
	private double atk;			//Attack power of this player
	private int numSpecialTurns;//Num of Special Turns
	private int position;       //Position in team

	/**
	 * Constructor of class Player, which initializes this player's type, maxHP, atk, numSpecialTurns, 
	 * as specified in the given table. It also reset the internal turn count of this player. 
	 * @param _type
	 */
	public Player(PlayerType _type)
	{	
		this(_type, 0, 0, 0, 0);
	}
	public Player(PlayerType type, double maxHP, double atk, int numSpecialTurns, int position)
	{
		this.type = type;
		this.maxHP = maxHP;
		this.atk = atk;
		this.numSpecialTurns = numSpecialTurns;
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
		result = a.currentHP - b.currentHP;
		if( result == 0 )
		{
			result = a.index - b.index;
		}
		return result;
	}
}

public class Healer extends Player
{
	public Healer()
	{
		this.maxHP = 4790;
		this.attack = 238;
		this.numSpecialTurns = 4;
		super(Player.PlayerType.Healer);
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
		min.currentHP = min.currentHP + ( this.maxHP * 0.25 );
		if( min.currentHP > this.maxHP )
		{
			min.currentHP = this.maxHP;
		}
	}
}