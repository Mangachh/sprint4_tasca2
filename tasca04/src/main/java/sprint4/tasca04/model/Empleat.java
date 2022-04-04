package sprint4.tasca04.model;


public class Empleat {

    private static int GENERAL_ID = 0;
    private int id;
    private String name;
    private String position;
    private double salary;

    

    public Empleat(String name, String position, double salary, int id) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.id = id;
        GENERAL_ID = id + 1;
    }

    public Empleat(String name, String position, double salary) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.id = GENERAL_ID;
        GENERAL_ID++;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public double getSalary() {
        return salary;
    }
    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
}
