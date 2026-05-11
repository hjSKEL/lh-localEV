/*******************************************************************************
 * Copyright(c) 2019 KEVIT All rights reserved.
 * This software is the proprietary information of KEVIT.
 *******************************************************************************/
package kr.co.kevit.localcsms.eai.vo.adr;

import java.util.List;

/**
 * 
    {
      "id": 0,
      "payloads": [
        {"type": "PRICE","values": [0.17]}
      ]
    }
    
    {
      "id": 1,
      "intervalPeriod": {
        "start": "0000-00-00",
        "duration": "PT2H"
      },
      "payloads": [
        {"type": "PRICE","values": [0.22]}
      ]
    }
 * @author bckim <a href="mailto:bckim@kevit.co.kr">bckim@kevit.co.kr</a> 
 * @since 2025. 1. 11.
 */
public class Interval {
    
    /**
    : A client generated number assigned an interval object. Not a sequence number. [0]
     */
    private int id;
    
    /**
    : Defines temporal aspects of intervals.
     */
    private IntervalPeriod intervalPeriod;
    
    /**
    : An array of payload objects.
    
    "payloads": [
        {"type": "PRIVATE_ALGORITHM","values": [0.17]},... 
    ]

     */
    private List<ValuesMap>payloads;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public IntervalPeriod getIntervalPeriod() {
        return intervalPeriod;
    }

    public void setIntervalPeriod(IntervalPeriod intervalPeriod) {
        this.intervalPeriod = intervalPeriod;
    }

    public List<ValuesMap> getPayloads() {
        return payloads;
    }

    public void setPayloads(List<ValuesMap> payloads) {
        this.payloads = payloads;
    }

}
