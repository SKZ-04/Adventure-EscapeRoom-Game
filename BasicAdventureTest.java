import java.io.IOException;
import java.util.List;

import AdventureModel.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicAdventureTest {
    @Test
    void getCommandsTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.player.getCurrentRoom().getCommands();
        assertEquals("DOWN,NORTH,IN,WEST,UP,SOUTH", commands);
    }

    @Test
    void getObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a water bird", objects);
    }

    //my tests
    @Test
    void GetCommmandsAndGetObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.player.getCurrentRoom().getCommands();
        String allObjectDescriptions = game.player.getCurrentRoom().getObjectString();
        assertEquals("WEST,UP,NORTH,IN,SOUTH,DOWN", commands);
        assertEquals("a water bird", allObjectDescriptions);
    }

    @Test
    void OnEnterTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        final boolean[] entered = {false};
        Room testRoom = new Room() {
            @Override
            public void OnEnter(Player player){
                System.out.println("Hello");
                entered[0] = true;
            }
        };
        game.getRooms().put(2, testRoom);
        game.movePlayer("west");
        assertTrue(entered[0]);
    }

    @Test
    void PlayerHealth() {
        Room room = new Room();
        Player player = new Player(room);
        player.takeDamage(3);
        assertEquals(player.getHealth(), 2);
        player.takeDamage(100);
        assertFalse(player.isAlive());
    }

    @Test
    void EnemyHealth() {
        Enemy e = new Enemy(6, "e");
        e.takeDamage(1);
        assertEquals(e.getHealth(), 5);
        e.takeDamage(5);
        assertFalse(e.isAlive());
    }

    @Test
    void EnemyParsing() throws IOException {
        AdventureGame g =  new AdventureGame("TinyGame");
        g.setUpGame();
        Room r = g.getRooms().get(4);
        Enemy e = r.getEnemy();
        assertNotEquals(e, null);
        assertEquals(e.getName(), "Barbarian");

        r = g.getRooms().get(5);
        e = r.getEnemy();
        assertNotEquals(e, null);
        assertEquals(e.getName(), "Goblin");
    }

}
