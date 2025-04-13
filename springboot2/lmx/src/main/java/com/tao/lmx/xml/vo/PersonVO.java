package com.tao.lmx.xml.vo;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@ToString
public class PersonVO {
    private String name;
    private int age;
    private String email;
    private AddressVO address;

    @XmlElementWrapper(name = "phoneNumbers")
    @XmlElement(name = "phone")
    private List<String> phoneNumbers;

    @XmlElementWrapper(name = "hobbies")
    @XmlElement(name = "hobby")
    private List<String> hobbies;

    @XmlAttribute
    private String id;

    @XmlTransient
    private String sensitiveData;

    // 중첩 클래스
    @XmlType
    @Data
    public static class AddressVO {
        private String street;
        private String city;
        private String zipCode;
        private String country;

        // getter, setter 생략
    }

    // 생성자
    public PersonVO() {}

    public PersonVO(String id, String name, int age, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.phoneNumbers = new ArrayList<>();
        this.hobbies = new ArrayList<>();
    }

    // getter, setter 생략
}