package com.example.springboottest.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paramresolve.config.RequestBodyExtract;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/test")
public class ControllerTest {
    @RequestMapping("/1")
    public void a(){
        System.out.println();
    }

    @RequestMapping("/2")
    public String test2(@RequestBodyExtract("lists") List<TestA> list){
        for (TestA testA : list) {
            System.out.println(testA.testb.get(0).testb);
            System.out.println(testA.getOrigin());
            System.out.println(testA);
        }
        return "success";
    }

    public static class Test{
        private String origin;

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }
    }

    public static class TestA extends Test{
        private String testa;
        private List<TestB> testb;

        public String getTesta() {
            return testa;
        }

        public void setTesta(String testa) {
            this.testa = testa;
        }

        public List<TestB> getTestb() {
            return testb;
        }

        public void setTestb(List<TestB> testb) {
            this.testb = testb;
        }

        @Override
        public String toString() {
            return "TestA{" +
                    "testa='" + testa + '\'' +
                    ", testb=" + testb +
                    '}';
        }
    }

    public static class TestB{
        private String testb;

        public String getTestb() {
            return testb;
        }

        public void setTestb(String testb) {
            this.testb = testb;
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        List<TestA> a = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TestA testA = new TestA();
            testA.setOrigin("origin" + i);
            testA.setTesta("testA" + i);
            TestB testB = new TestB();
            testB.setTestb("testB" + i);
            testA.setTestb(Arrays.asList(testB));
            a.add(testA);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("lists", a);
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(map));
    }
}
