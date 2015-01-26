/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tjm.setting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author larry
 */
@Controller
@RequestMapping("/setting/auth")
public class AuthController {
    
	@RequestMapping("/")
	public ModelAndView mainPage(){
		ModelAndView mav = new ModelAndView("/setting/auth/main");
		
		System.out.println("/setting/auth/main");
		
		
		return mav;
	}
	
}
