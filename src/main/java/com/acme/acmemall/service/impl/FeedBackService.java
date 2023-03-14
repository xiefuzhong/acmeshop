package com.acme.acmemall.service.impl;

import com.acme.acmemall.dao.FeedbackMapper;
import com.acme.acmemall.model.FeedbackVo;
import com.acme.acmemall.service.IFeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: ihpangzi
 * @time: 2023/3/14 9:27
 */
@Service
public class FeedBackService implements IFeedBackService {

    @Autowired
    FeedbackMapper feedbackMapper;

    /**
     * @param feedback
     */
    @Override
    public void feedBack(FeedbackVo feedback) {
        feedbackMapper.save(feedback);
    }
}
