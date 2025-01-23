package com.ecs160;

public class Student {
    @MyAnnotation
    private Integer id;
    @MyAnnotation
    private String name;
    private Boolean active;


    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.active = true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    public void deactivate() {
        this.active = false;
    }

     */

    public Boolean getActive() {
        return active;
    }
}
