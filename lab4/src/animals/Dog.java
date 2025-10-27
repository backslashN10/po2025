package animals;

public class Dog extends Animal {
	
    public Dog(String name) {
        super(name, 4);
    }

    @Override
    public String getDescription() {
        return "This is a dog '" + name + " ' with " + legs + " legs.";
    }
}