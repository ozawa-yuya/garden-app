package com.example.gardenapp.form;

import java.util.List;

import com.example.gardenapp.model.VegetableSelection;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class InputForm {
    @NotNull(message = "農地の幅(cm)を入力してください")
    @Min(value = 1, message = "1cm以上必要です")
    @Max(value = 999, message = "最大999cmです")
    private Integer farmlandWidth;
    @NotNull(message = "農地の奥行(cm)を入力してください")
    @Min(value = 1, message = "1cm以上必要です")
    @Max(value = 999, message = "最大999cmです")
    private Integer farmlandHeight;
    @NotNull(message = "農地の余白(cm)を入力してください")
    @Min(value = 0, message = "0cm以上必要です")
    @Max(value = 999, message = "最大999cmです")
    private Integer farmlandOffset;
    @NotNull(message = "畝の幅(cm)を入力してください")
    @Min(value = 1, message = "1cm以上必要です")
    @Max(value = 999, message = "最大999cmです")
    private Integer ridgeWidth;
    @NotNull(message = "畝の奥行(cm)を入力してください")
    @Min(value = 1, message = "1cm以上必要です")
    @Max(value = 999, message = "最大999cmです")
    private Integer ridgeHeight;
    @NotNull(message = "畝間の間隔(cm)を入力してください")
    @Min(value = 1, message = "1cm以上必要です")
    @Max(value = 999, message = "最大999cmです")
    private Integer ridgeGap;
    @Valid
    private List<VegetableSelection> selections;
}
