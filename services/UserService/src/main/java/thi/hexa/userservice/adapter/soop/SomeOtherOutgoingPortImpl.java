package thi.hexa.userservice.adapter.soop;

import org.springframework.stereotype.Service;

@Service
public class SomeOtherOutgoingPortImpl implements thi.hexa.userservice.ports.outgoing.SomeOtherOutgoingPort {
    @Override
    public void getInfo(String request) {
        System.out.println("SomeOtherOutgoingPort was called with request:"+ request);
    }
}
