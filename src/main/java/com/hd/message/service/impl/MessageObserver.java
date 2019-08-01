package com.hd.message.service.impl;

import com.hd.message.dto.MessageDTO;
import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;

/**
 * 被观察者
 *
 * @author wang_yw
 */
@Component
public class MessageObserver extends Observable {

    public void registerObserver(Observer observer) {
        addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        removeObserver(observer);
    }

    /**
     * 清楚所有的观察者
     */
    @Override
    public void deleteObservers() {
        deleteObservers();
    }


    @Override
    public void setChanged() {
        super.setChanged();
    }

    public void notifyAllObserver(MessageDTO messageDTO) {
        notifyObservers(messageDTO);
    }
}
