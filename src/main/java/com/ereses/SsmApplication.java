package com.ereses;

import com.ereses.ssm.enums.MyEvent;
import com.ereses.ssm.enums.MyState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.statemachine.StateMachine;


@SpringBootApplication
public class SsmApplication implements CommandLineRunner {

	@Autowired
	private StateMachine<MyState, MyEvent> myStateMachine;

	@Qualifier("mySecondStateMachine")
	@Autowired
	private StateMachine<MyState, MyEvent> mySecondStateMachine;

	@Qualifier("myThirdStateMachine")
	@Autowired
	private StateMachine<MyState, MyEvent> myOtherStateMachine;

	@Autowired
	ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(SsmApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		myStateMachine.sendEvent(MyEvent.E1);
		myStateMachine.sendEvent(MyEvent.E2);
		myStateMachine.sendEvent(MyEvent.E3);
		myStateMachine.sendEvent(MyEvent.E1);

		mySecondStateMachine.start();
		mySecondStateMachine.sendEvent(MyEvent.E1);
		mySecondStateMachine.sendEvent(MyEvent.E2);
		mySecondStateMachine.sendEvent(MyEvent.E3);
		mySecondStateMachine.sendEvent(MyEvent.E1);

		myOtherStateMachine.start();
		myOtherStateMachine.sendEvent(MyEvent.E1);
		myOtherStateMachine.sendEvent(MyEvent.E2);
		myOtherStateMachine.sendEvent(MyEvent.E3);
		myOtherStateMachine.sendEvent(MyEvent.E1);
	}
}
