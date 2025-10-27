package animals;

public abstract class Animal {

    protected String name;
    protected int legs;

    public Animal(String name, int legs) {
        this.name = name;
        this.legs = legs;
    }
    public int getLegs() {
    	return legs;
    }
    public String getName() {
    	return name;
    }
    public abstract String getDescription();
}
