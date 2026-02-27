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
import com.example.gardenapp.model.PlanningResult;
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
    public String inputView(Model model, HttpSession httpSession) {
        InputForm inputForm = (InputForm) httpSession.getAttribute("inputForm");
        List<Vegetable> vegetableList = vegetableService.getVegetableList();

        if (inputForm == null) {
            inputForm = new InputForm();
            inputForm.setSelections(new ArrayList<>());
        }

        List<VegetableSelection> selections = inputForm.getSelections();
        if (selections == null) {
            selections = new ArrayList<>();
        }
        while (selections.size() < vegetableList.size()) {
            selections.add(new VegetableSelection());
        }
        inputForm.setSelections(selections);

        model.addAttribute("inputForm", inputForm);
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
                }
            }
        }

        if (bindingResult.hasErrors()) {
            return "index";
        }

        httpSession.setAttribute("inputForm", inputForm);
        // inputFormデータを処理
        List<PlantArea> plantAreaList = plantAreaService.creatPlantAreaList(inputForm);
        Farmland farmland = farmlandService.generateRidge(inputForm);
        PlanningResult planningResult = planner.plan(plantAreaList, farmland);
        Farmland calculatedFarmland = planningResult.getFarmland();
        model.addAttribute("farmland", calculatedFarmland);
        model.addAttribute("unplacedVegetables", planningResult.getUnplacedVegetables());
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

    @GetMapping("/reset")
    public String reset(HttpSession httpSession) {
        httpSession.removeAttribute("inputForm");
        httpSession.removeAttribute("farmland");
        return "redirect:/";
    }
}
