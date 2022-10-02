package com.quangtung.springtest.repository;

import com.quangtung.springtest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByEmail(String email);

    @Query("select e from Employee e where e.lastName=?1 and e.firstName=?2")
    Optional<Employee> findEmployeeByJpql(String lastName, String firstName);

    @Query("select e from Employee e where e.email =:email")
    Optional<Employee> findEmployeeByEmailJpql(@Param("email") String email);

    @Query(value = "select * from employees e where e.first_name=?1", nativeQuery = true)
    Optional<Employee> findEmployeeByFirstNameJpql(String firstName);

    @Query(value = "select * from employees e where e.first_name=:firstName", nativeQuery = true)
    Optional<Employee> findEmployeeByFirstNameNative(@Param("firstName") String firstName);
}
