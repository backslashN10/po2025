package animals;

public class Snake extends Animal {
	
    public Snake(String name) {
        super(name, 0);
    }

    @Override
    public String getDescription() {
        return "This is a snake '" + name + " ' with " + legs + " legs.";
    }
}