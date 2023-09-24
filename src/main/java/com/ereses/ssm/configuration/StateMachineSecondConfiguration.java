package com.ereses.ssm.configuration;

import com.ereses.ssm.enums.MyEvent;
import com.ereses.ssm.enums.MyState;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
public class StateMachineSecondConfiguration {

    @Qualifier("mySecondStateMachine")
    @Bean
    public StateMachine<MyState, MyEvent> configStateMachine() throws Exception {
        StateMachineBuilder.Builder<MyState, MyEvent> stateMachineBuilder = StateMachineBuilder.builder();

        stateMachineBuilder
                .configureStates()
                .withStates()
                .initial(MyState.SI)
                .states(EnumSet.allOf(MyState.class));

        stateMachineBuilder
                .configureConfiguration()
                .withConfiguration()
                .machineId("mySecondStateMachine")
//                .autoStartup(true)
                .listener(mySecondListener());

        stateMachineBuilder
                .configureTransitions()
                .withExternal()
                .source(MyState.SI).target(MyState.S1).event(MyEvent.E1)

                .and()
                .withExternal()
                .source(MyState.S1).target(MyState.S2).event(MyEvent.E2)

                .and()
                .withExternal()
                .source(MyState.S2).target(MyState.S3).event(MyEvent.E3)

                .and()
                .withExternal()
                .source(MyState.S3).target(MyState.SI).event(MyEvent.E1);

        return stateMachineBuilder.build();
    }

//    @Override
//    public void configure(StateMachineConfigurationConfigurer<MyState, MyEvent> config) throws Exception {
//        config.withConfiguration()
//                .machineId("mySecondStateMachine")
////                .autoStartup(true)
//                .listener(mySecondListener());
//    }

//    @Override
//    public void configure(StateMachineStateConfigurer<MyState, MyEvent> state) throws Exception {
//        state.withStates()
//                .initial(MyState.SI)
//                .states(EnumSet.allOf(MyState.class));
//    }

//    @Override
//    public void configure(StateMachineTransitionConfigurer<MyState, MyEvent> transitions)
//            throws Exception {
//        transitions
//                .withExternal()
//                .source(MyState.SI).target(MyState.S1).event(MyEvent.E1)
//
//                .and()
//                .withExternal()
//                .source(MyState.S1).target(MyState.S2).event(MyEvent.E2)
//
//                .and()
//                .withExternal()
//                .source(MyState.S2).target(MyState.S3).event(MyEvent.E3)
//
//                .and()
//                .withExternal()
//                .source(MyState.S3).target(MyState.SI).event(MyEvent.E1);
//    }

    @Bean
    public StateMachineListener<MyState, MyEvent> mySecondListener() {
        return new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<MyState, MyEvent> from, State<MyState, MyEvent> to) {
                System.out.println("My Second State Machine MyState change to " + to.getId());
                // System.out.println("MyState change from " + from.getId() + " to " + to.getId());
            }
        };
    }
}
