package DataControllerTests;

import org.junit.Test;

import DataControllers.Horse;

import static org.junit.Assert.*;


public class HorseTest {
    @Test
    public void testHorseSetters() throws Exception {
        Horse h = new Horse();
        h.setMedicationInstructions("hella drugs");
        h.setStallInstructions("feed hourly");
        h.setHay(true);
        h.setNotes("beautiful");
        h.setGrainType("oats");
        h.setBreed("great looking");
        h.setColor("white");
        h.setGrainAmount("a lot");
        h.setName("brad");
        h.setSex("male");
        h.setStallNumber(69);

        assertEquals(h.getMedicationInstructions(), "hella drugs");
        assertEquals(h.getStallInstructions(), "feed hourly");
        assertEquals(h.getHay(), true);
        assertEquals(h.getNotes(), "beautiful");
        assertEquals(h.getGrainType(), "oats");
        assertEquals(h.getBreed(), "great looking");
        assertEquals(h.getColor(), "white");
        assertEquals(h.getGrainAmount(), "a lot");
        assertEquals(h.getName(), "brad");
        assertEquals(h.getSex(), "male");
        assertEquals(h.getStallNumber(), 69);

    }

}