package in.grasshoper.field.tag.api;

import in.grasshoper.core.infra.ApiSerializer;
import in.grasshoper.core.infra.CommandProcessingResult;
import in.grasshoper.core.infra.FromJsonHelper;
import in.grasshoper.core.infra.JsonCommand;
import in.grasshoper.core.security.service.PlatformSecurityContext;
import in.grasshoper.field.tag.data.SubTagData;
import in.grasshoper.field.tag.data.TagData;
import in.grasshoper.field.tag.service.SubTagWriteService;
import in.grasshoper.field.tag.service.TagReadService;
import in.grasshoper.field.tag.service.TagWriteService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonParser;

@RestController  
@RequestMapping("/tag")
public class TagApiResource {
	
	private final TagWriteService tagWriteService;
	private final SubTagWriteService subTagWriteService;
	private final TagReadService tagReadService;
	private final ApiSerializer<TagData> apiJsonTagSerializerService;
	private final ApiSerializer<SubTagData> apiJsonSubTagSerializerService;
	private final FromJsonHelper fromApiJsonHelper;
	private final PlatformSecurityContext context;
	@Autowired
	private TagApiResource(TagWriteService tagWriteService,
			SubTagWriteService subTagWriteService,
			TagReadService tagReadService,
			ApiSerializer<TagData> apiJsonTagSerializerService,
			ApiSerializer<SubTagData> apiJsonSubTagSerializerService,
			final FromJsonHelper fromApiJsonHelper,
			final PlatformSecurityContext context) {
		super();
		this.tagWriteService = tagWriteService;
		this.subTagWriteService = subTagWriteService;
		this.tagReadService = tagReadService;
		this.apiJsonTagSerializerService = apiJsonTagSerializerService;
		this.apiJsonSubTagSerializerService = apiJsonSubTagSerializerService;
		this.fromApiJsonHelper = fromApiJsonHelper;
		this.context = context;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
    public CommandProcessingResult createTag(@RequestBody final  String reqBody){
		this.context.restrictPublicUser();
		return this.tagWriteService.createTag(JsonCommand.from(reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
    public String getAllTags() {
		this.context.restrictPublicUser();
		Collection<TagData> result = this.tagReadService.retriveAllTags();
		return apiJsonTagSerializerService.serialize(result);
	}
	
	@RequestMapping(value="/{tagId}", method = RequestMethod.GET)
	@ResponseBody
    public String getTag(@PathVariable("tagId") final Long tagId) {
		this.context.restrictPublicUser();
		TagData result = this.tagReadService.retriveOneTag(tagId);
		return apiJsonTagSerializerService.serialize(result);
	}
	@RequestMapping(value="/{tagId}", method = RequestMethod.PUT)
	@ResponseBody
    public CommandProcessingResult updateTag(@PathVariable("tagId") final Long tagId,
    		@RequestBody final  String reqBody) {
		this.context.restrictPublicUser();
		return this.tagWriteService.updateTag(tagId, JsonCommand.from(tagId, reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
	}
	@RequestMapping(value="/{tagId}", method = RequestMethod.DELETE)
	@ResponseBody
    public CommandProcessingResult removeTag(@PathVariable("tagId") final Long tagId) {
		this.context.restrictPublicUser();
		return this.tagWriteService.removeTag(tagId);
	}
	
	@RequestMapping(value="/{tagId}/subtag", method = RequestMethod.POST)
	@ResponseBody
    public CommandProcessingResult createSubTagForTag(@PathVariable("tagId") final Long tagId,
    		@RequestBody final  String reqBody) {
		this.context.restrictPublicUser();
		return this.subTagWriteService.createSubTag(JsonCommand.from(tagId, reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
	}
	
	@RequestMapping(value="/{tagId}/subtag", method = RequestMethod.GET)
	@ResponseBody
    public String getAllSubTagsForTag(@PathVariable("tagId") final Long tagId) {
		this.context.restrictPublicUser();
		Collection<SubTagData> result = this.tagReadService.retriveAllSubTagsForTag(tagId);
		return apiJsonSubTagSerializerService.serialize(result);
	}
	/*@RequestMapping(value="/subtag/{subTagId}", method = RequestMethod.GET)
	@ResponseBody
    public String getSubTag(@PathVariable("subTagId") final Long subTagId) {
		SubTagData result = this.tagReadService.retriveOneSubTag(subTagId);
		return apiJsonSubTagSerializerService.serialize(result);
	}*/
	
	@RequestMapping(value="{tagId}/subtag/{subTagId}", method = RequestMethod.GET)
	@ResponseBody
    public String getSubTagwithTag(@PathVariable("subTagId") final Long subTagId) {
		this.context.restrictPublicUser();
		SubTagData result = this.tagReadService.retriveOneSubTag(subTagId);
		return apiJsonSubTagSerializerService.serialize(result);
	}
	
	@RequestMapping(value="{tagId}/subtag/{subTagId}", method = RequestMethod.PUT)
	@ResponseBody
    public CommandProcessingResult upateSubTagwithTag(@PathVariable("subTagId") final Long subTagId,
    		@RequestBody final  String reqBody) {
		this.context.restrictPublicUser();
		return this.subTagWriteService.updateSubTag(subTagId, JsonCommand.from(subTagId, reqBody,
				new JsonParser().parse(reqBody), fromApiJsonHelper));
	}
	
	@RequestMapping(value="{tagId}/subtag/{subTagId}", method = RequestMethod.DELETE)
	@ResponseBody
    public CommandProcessingResult removeSubTagwithTag(@PathVariable("subTagId") final Long subTagId) {
		this.context.restrictPublicUser();
		return this.subTagWriteService.removeSubTag(subTagId);
	}
}
