package sprint4.tasca04.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import sprint4.tasca04.model.Empleat;

@Repository
public class EmpleatRepo {
    
    List<Empleat> empleats;

    public EmpleatRepo(){
        this.empleats = new ArrayList<Empleat>();
        this.createEmpleats();
    }

    private void createEmpleats(){
        this.empleats.add(new Empleat("Empleat 1", "Jefe", 200.50, 1));
        this.empleats.add(new Empleat("Empleat 2", "Limpieza", 40.80, 2));
        this.empleats.add(new Empleat("Empleat 3", "IT", 875.12, 3));
        this.empleats.add(new Empleat("Empleat 4", "Currela", 21.54, 4));
    }


    public List<Empleat> getEmpleats(){
        return this.empleats;
    }

    public Empleat getEmpleat(int id){
        Optional<Empleat> opt = this.empleats.stream().filter(em -> em.getId() == id).findFirst();

        if(opt.isPresent()){
            return opt.get();
        }

        return null;
    }

    public int getEmpleatsCount(){
        return this.empleats.size();
    }

    public void addEmpleat(final String name, final String position, final double salary){
        empleats.add(new Empleat(name, position, salary));
    }

    public void addEmpleat(final Empleat empleat){
        this.empleats.add(empleat);
    }

    // si se quita bien, devolvemos true
    public boolean removeEmpleat(int id){
        Optional<Empleat> opt = this.empleats.stream().filter(em -> em.getId() == id).findFirst();

        if(opt.isPresent()){            
            this.empleats.remove(opt.get());
            return true;
        }

        return false;
    }

    public List<Empleat> getEmpleatByPosition(final String position){
        List<Empleat> empleats = this.empleats.stream().filter(em -> em.getPosition().equalsIgnoreCase(position)).toList();
        return empleats;
    }

    public boolean updateEmpleat(Integer id, String name, String position, Double salary) {
        Optional<Empleat> opt = this.empleats.stream().filter(em -> em.getId() == id).findFirst();
    
        if(opt.isPresent()){
            Empleat e = opt.get();
            if(name != null){
                e.setName(name);
            }
            
            if(position != null){
                e.setPosition(position);
            }
            
            if(salary != null){
                e.setSalary(salary);
            }

            return true;
        }

        return false;
    }

    
}
