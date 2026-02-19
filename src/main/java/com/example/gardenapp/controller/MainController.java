package com.example.gardenapp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.gardenapp.form.InputForm;
import com.example.gardenapp.model.Farmland;
import com.example.gardenapp.model.PlantArea;
import com.example.gardenapp.model.Vegetable;
import com.example.gardenapp.model.VegetableSelection;
import com.example.gardenapp.service.FarmlandService;
import com.example.gardenapp.service.Planner;
import com.example.gardenapp.service.PlantAreaService;
import com.example.gardenapp.service.VegetableService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final VegetableService vegetableService;
    private final FarmlandService farmlandService;
    private final PlantAreaService plantAreaService;
    private final Planner planner;

    @ModelAttribute("vegetableList")
    public List<Vegetable> setUpVegetableList() {
        return vegetableService.getVegetableList();
    }

    @GetMapping({ "/", "/index" })
    public String inputView(InputForm inputForm) {
        // リストが空（初回アクセス時）なら、10行分のインスタンスを作成してセットする
        if (inputForm.getSelections() == null || inputForm.getSelections().isEmpty()) {
            List<VegetableSelection> selections = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                selections.add(new VegetableSelection());
            }
            inputForm.setSelections(selections);
        }
        return "index";
    }

    @PostMapping("/index/result")
    public String resultView(@Validated InputForm inputForm, BindingResult bindingResult, Model model,
            HttpSession httpSession) {
        // 入力がある行だけを対象に手動バリデーションを行う
        if (inputForm.getSelections() != null) {
            for (int i = 0; i < inputForm.getSelections().size(); i++) {
                VegetableSelection s = inputForm.getSelections().get(i);
                // 野菜が選ばれている場合のみチェック
                if (s.getVegetableId() != null) {
                    if (s.getQuantity() == null || s.getQuantity() < 1) {
                        bindingResult.rejectValue("selections[" + i + "].quantity", "error.quantity", "株数を入力してください");
                    }
                    if (s.getRowsCount() == null || s.getRowsCount() < 1) {
                        bindingResult.rejectValue("selections[" + i + "].rowsCount", "error.rowsCount", "条数を入力してください");
                    }
                }
            }
        }

        if (bindingResult.hasErrors()) {
            return "index";
        }

        // エラーがない場合のみ、空行を除去して result 画面へ
        inputForm.getSelections().removeIf(s -> s.getVegetableId() == null);
        // inputFormデータを処理
        List<PlantArea> plantAreaList = plantAreaService.creatPlantAreaList(inputForm);
        Farmland farmland = farmlandService.generateRidge(inputForm);
        Farmland calculatedFarmland = planner.plan(plantAreaList, farmland);
        model.addAttribute("farmland", calculatedFarmland);
        httpSession.setAttribute("farmland", calculatedFarmland);

        return "result";
    }

    @PostMapping("/index/save")
    public String resultSaveView(HttpSession httpSession, RedirectAttributes redirectAttributes) {
        Farmland farmland = (Farmland) httpSession.getAttribute("farmland");
        if (farmland != null) {
            farmlandService.saveFullPlan(farmland);
            httpSession.removeAttribute("farmland");
        }
        redirectAttributes.addFlashAttribute("msg", "結果を保存しました");
        return "redirect:/";
    }
}
