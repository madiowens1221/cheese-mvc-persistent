package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.data.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by Jeremy on 6/26/17.
 */

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;

    //index handler
    @RequestMapping(value = "")
    public String index (Model model) {

        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("title", "Categories");

        return "category/index";
    }

    //add handler
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCategoryForm (Model model) {

        model.addAttribute(new Category());
        model.addAttribute("title", "Add Category");

        return "category/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCategoryForm (Model model, @ModelAttribute @Valid Category category, Errors errors) {

        if(errors.hasErrors()) {
            model.addAttribute("errors", errors);
            return "category/add";

        } else {
            categoryDao.save(category);
            return "redirect:";
        }
    }



}