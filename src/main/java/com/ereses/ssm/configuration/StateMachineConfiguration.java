package com.ereses.ssm.configuration;

import com.ereses.ssm.enums.MyEvent;
import com.ereses.ssm.enums.MyState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachine(name = "myStateMachine")
public class StateMachineConfiguration extends EnumStateMachineConfigurerAdapter<MyState, MyEvent> {

    @Override
    public void configure(
            StateMachineConfigurationConfigurer<MyState, MyEvent> config
            ) throws Exception {
        config.withConfiguration()
//                .machineId("myStateMachine")
                .autoStartup(true)
                .listener(myListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<MyState, MyEvent> state) throws Exception {
        state.withStates()
                .initial(MyState.SI)
                .end(MyState.SF)
                .states(EnumSet.allOf(MyState.class))
                .stateEntry(MyState.S2, secondEntryAction()) // sebelum masuk
                .state(MyState.S2, secondExecuteAction()) // saat sudah masuk
                .stateExit(MyState.S2, secondExitAction()) // saat keluar

        ;
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<MyState, MyEvent> transitions)
            throws Exception {
        transitions
                .withExternal()
                .source(MyState.SI).target(MyState.S1).event(MyEvent.E1).action(initAction(), initErrorAction())

                .and()
                .withExternal()
                .source(MyState.S1).target(MyState.S2).event(MyEvent.E2).action(firstToSecondAction(), secondErrorAction())

                .and()
                .withExternal()
                .source(MyState.S2).target(MyState.S3).event(MyEvent.E3)

                .and()
                .withExternal()
                .source(MyState.S3).target(MyState.SI).event(MyEvent.E1);
    }

    // Global Listener
    public StateMachineListener<MyState, MyEvent> myListener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<MyState, MyEvent> from, State<MyState, MyEvent> to) {
                System.out.println("MyState change to " + to.getId());
                // System.out.println("MyState change from " + from.getId() + " to " + to.getId());
            }
        };
    }

    public Action<MyState, MyEvent> initAction() {
        return new Action<MyState, MyEvent>() {
            @Override
            public void execute(StateContext<MyState, MyEvent> stateContext) {
                System.out.println("MyState init next state: " + stateContext.getTarget().getId());
                // Test throw error
//                throw new NoSuchElementException("Something not found!");
            }
        };
    }

    public Action<MyState, MyEvent> initErrorAction() {
        return new Action<MyState, MyEvent>() {
            @Override
            public void execute(StateContext<MyState, MyEvent> stateContext) {
                System.out.println("MyState init exception: " + stateContext.getException());
            }
        };
    }

    public Action<MyState, MyEvent> secondEntryAction() {
        return new Action<MyState, MyEvent>() {
            @Override
            public void execute(StateContext<MyState, MyEvent> stateContext) {
                System.out.println("MyState secondEntryAction");
            }
        };
    }

    public Action<MyState, MyEvent> secondExecuteAction() {
        return new Action<MyState, MyEvent>() {
            @Override
            public void execute(StateContext<MyState, MyEvent> stateContext) {
                System.out.println("MyState secondExecuteAction");
//                throw new IllegalArgumentException("Something is illegal");
            }
        };
    }

    public Action<MyState, MyEvent> secondExitAction() {
        return new Action<MyState, MyEvent>() {
            @Override
            public void execute(StateContext<MyState, MyEvent> stateContext) {
                System.out.println("MyState secondExitAction");
            }
        };
    }

    public Action<MyState, MyEvent> firstToSecondAction() {
        return new Action<MyState, MyEvent>() {
            @Override
            public void execute(StateContext<MyState, MyEvent> stateContext) {
                System.out.println("MyState action from " + stateContext.getSource().getId() + " to " + stateContext.getTarget().getId() + " with event " + stateContext.getEvent().name());
            }
        };
    }

    public Action<MyState, MyEvent> secondErrorAction() {
        return new Action<MyState, MyEvent>() {
            @Override
            public void execute(StateContext<MyState, MyEvent> stateContext) {
                System.out.println("MyState second exception: " + stateContext.getException());
            }
        };
    }
}
