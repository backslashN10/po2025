package animals;

public class Parrot extends Animal {
	
    public Parrot(String name) {
        super(name, 2);
    }

    @Override
    public String getDescription() {
        return "This is a parrot '" + name + " ' with " + legs + " legs.";
    }
}