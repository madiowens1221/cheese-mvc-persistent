package org.launchcode.controllers;


import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.addMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by Jeremy on 6/26/17.
 */

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");

        return "menu/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddMenuForm(Model model) {

        model.addAttribute(new Menu());
        model.addAttribute("title", "Add Menu");

        return "menu/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddMenuForm (Model model, @ModelAttribute @Valid Menu menu, Errors errors) {

        if (errors.hasErrors()) {
            model.addAttribute("errors", errors);
            return "menu/add";

        } else {
            menuDao.save(menu);
            return "redirect:view" + menu.getId();
        }
    }

    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String displayViewMenuForm(@PathVariable int menuId, Model model) {

            model.addAttribute("menu", menuDao.findOne(menuId));
            model.addAttribute("title", menuDao.findOne(menuId).getName());

        return "menu/view";

    }

    @RequestMapping(value = "add-item/{menuId}", method = RequestMethod.GET)
    public String additem(@PathVariable int menuId, Model model) {

        Menu menu = menuDao.findOne(menuId);
        Iterable<Cheese> cheeses = cheeseDao.findAll();
        addMenuItemForm newMenu = new addMenuItemForm(menu, cheeses);

        model.addAttribute("form", newMenu);
        model.addAttribute("title", "Add Item to Menu: " + menu.getName());
        return "menu/add-item";
    }

    @RequestMapping(value = "add-item", method = RequestMethod.POST)
    public String addItem(@ModelAttribute @Valid addMenuItemForm form, Errors errors, Model model) {

        if(errors.hasErrors()) {
            return "menu/add-item";
        } else {
            Menu menu = menuDao.findOne(form.getMenuId());
            Cheese cheese = cheeseDao.findOne(form.getCheeseId());

            menu.addItem(cheese);

            menuDao.save(menu);

            return "redirect:view/" + menu.getId();
        }

    }
}
