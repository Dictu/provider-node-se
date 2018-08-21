package eu.toop.node.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.toop.node.model.DataSet;
import eu.toop.node.provider.ProviderService;

@RestController
public class ToopProvider {
	
	@Autowired
	private ProviderService providerService;
	
	@RequestMapping("/provider/provide")
    public DataSet provide(@RequestParam(value="id") String id) {
        return providerService.provide(id);
    }
	
}
