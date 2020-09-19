package com.chenxin.controller;

import cn.hutool.core.util.StrUtil;
import com.chenxin.base.BaseController;
import com.chenxin.model.R;
import com.chenxin.model.ReqBody;
import com.chenxin.model.dto.LexerOut;
import com.chenxin.model.dto.TextDto;
import com.chenxin.service.LexerService;
import com.chenxin.util.CommonEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 词义分析控制层
 * Created by 尘心 on 2020/9/19 0019.
 */
@Api(tags = "NLP自然语言处理接口")
@RestController
@RequestMapping("/lexer")
public class LexerController extends BaseController{

    @Autowired
    private LexerService lexerService;

    @ApiOperation("文本分词")
    @PostMapping("/lexerText")
    public R lexerText(@RequestBody ReqBody<TextDto> para) {
        if (para == null||para.getParams() == null) {
            return R.error(CommonEnum.PARAM_ERROR);
        }

        String accessToken = getAccessToken();
        if (StrUtil.isBlank(accessToken)) {
            return R.error(CommonEnum.TOKEN_ERROR);
        }

        return R.success(lexerService.analyseLexer(para.getParams(),accessToken));
    }

    @ApiOperation("文本'变脸' 即伪原创")
    @PostMapping("/textReplace")
    public R wordReplace(@RequestBody ReqBody<TextDto> para) {
        if (para == null||para.getParams() == null) {
            return R.error(CommonEnum.PARAM_ERROR);
        }

        String accessToken = getAccessToken();
        if (StrUtil.isBlank(accessToken)) {
            return R.error(CommonEnum.TOKEN_ERROR);
        }

        LexerOut lexerOut = lexerService.analyseLexer(para.getParams(),accessToken);
        if (lexerOut == null) {
            return R.error(CommonEnum.ANALYSE_WORDS_FAIL);
        }

        // DNN语言模型校验 TODO

        return R.success(lexerService.sliceSentence(lexerOut));
    }

}