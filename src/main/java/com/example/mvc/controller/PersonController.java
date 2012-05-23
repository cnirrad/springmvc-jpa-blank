package com.example.mvc.controller;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.mvc.controller.util.Range;
import com.example.mvc.entity.Person;
import com.example.mvc.service.PersonService;

@Controller
@RequestMapping("/person")
public class PersonController {
    protected static final int DEFAULT_PAGE_NUM = 0;
    protected static final int DEFAULT_PAGE_SIZE = 10;

    private static final String RANGE_HEADER = "X-Range";
    private static final String CONTENT_RANGE_HEADER = "Content-Range";
    
    @Inject
    protected PersonService personService;

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(PersonController.class);

    @RequestMapping(value = "/list")
    public String list() {
        return "/person/list";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public @ModelAttribute
    Person create(Model model) {
        Person person = new Person();
        return person;
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String createOnSubmit(@Valid Person person,
            BindingResult bindingResult, Model model) {
        LOGGER.debug("create person={}", person);
        if (bindingResult.hasErrors()) {
            LOGGER.warn("validation error={}", bindingResult.getModel());
            model.addAllAttributes(bindingResult.getModel());
            return "/person/form";
        }
        personService.insert(person);
        return "redirect:/person/list";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") Integer id, Model model) {
        Person person = personService.findById(id);
        model.addAttribute(person);
        return "/person/form";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String editOnSubmit(@Valid Person person,
            BindingResult bindingResult, Model model) {
        LOGGER.debug("edit person={}", person);
        if (bindingResult.hasErrors()) {
            LOGGER.warn("validation error={}", bindingResult.getModel());
            model.addAllAttributes(bindingResult.getModel());
            return "/person/form";
        }
        personService.update(person);
        return "redirect:/person/list";
    }

    @RequestMapping(value = "/delete/{id}")
    public String delete(
            @RequestParam(value = "page", required = false) Integer page,
            @PathVariable("id") Integer id) {
        LOGGER.debug("delete id={}", id);
        personService.deleteById(id);

        return "redirect:/person/list";
    }
    
    @RequestMapping(value = "/query", method=RequestMethod.GET)
    public @ResponseBody HttpEntity<List<Person>> query(
                                    @RequestHeader(value=RANGE_HEADER, required=false) String rangeParam,
                                    HttpServletRequest request) {
        
        //
        //  Loop through the HTTP parameters to find the sort.
        //
        Sort sort = null;
        Set<?> parameters = request.getParameterMap().keySet();
        for(Object paramObj : parameters){
            if (((String)paramObj).startsWith("sort(")) {
                String param = (String)paramObj;
                
                String sortAttr = param.substring("sort(".length(), param.length() - 1);
                Direction direction = Direction.ASC;
                if (sortAttr.startsWith("-")) {
                    direction = Direction.DESC;
                }
                sortAttr = sortAttr.substring(1);
                sort = new Sort(direction, sortAttr);
                break;
            }
        }
        
        //
        // If no sort given, then use default
        //
        if (sort == null) {
            sort = new Sort(Direction.ASC, "id");
        }
        
        Range range = Range.parseRange(rangeParam);
        Page<Person> page = personService.findAll(sort, range.getPage(), range.getPageSize());
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Retrieved page {} with elements {} - {} of {} using sort {}", 
                    new Object[] {range.getPage(), range.getStart(), range.getEnd(), page.getTotalElements(), sort });
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_RANGE_HEADER, 
                getContentRangeValue(range.getStart(), page.getNumberOfElements(), page.getTotalElements()));
        
        return new HttpEntity<List<Person>>(page.getContent(), headers);
    }

    private String getContentRangeValue(int firstResult, int resultCount, long totalCount) {
        StringBuilder value = new StringBuilder("items "+firstResult+"-");
        if (resultCount == 0) {
            value.append("0");
        } else {
            value.append(firstResult + resultCount - 1);
        }
        value.append("/"+totalCount);
        return value.toString();
    }
}
