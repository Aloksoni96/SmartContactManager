package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.smart.dao.ContactRepositary;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepositary contactRepositary;
	
	
	// method for adding common data for response (user )
	@ModelAttribute
	public void addCommonData(Model model , Principal principal) {
		
		String userName = principal.getName();
		System.out.println("Username"+ userName);
		
		User user = this.userRepository.getUserByUserName(userName);
		System.out.println("user Deatils :- "+ user);
		
		model.addAttribute("user", user);
		
	}
	
	//dashboard home
	@RequestMapping("/index")
	public String index(Model model, Principal principal) {
		model.addAttribute("tittle", "Home");
		
		
		return "normal/user_dashboard";
	}
	
	// open add form handler
	
	@RequestMapping("/add-contact")
	private String openAddContactForm(Model model) {
		
		model.addAttribute("tittle", "Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
	// processing add contact form 
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file , Principal principal, HttpSession session) {
		
		try {
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			
			//processing & uploading file 
			
			/*
			 * if(3>2) { throw new Exception(); }
			 */
			if(file.isEmpty()) {
				
				//if the file is empty then try our messeage
				System.out.println("File is Emplty");
				contact.setImage("contact.png");
			}
			else {
				
				//upload file to the folder & update the name to the contact
				contact.setImage(file.getOriginalFilename());
				
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				System.out.println("Image is Uploaded ");
			}
			
			contact.setUser(user); 
			
			user.getContacts().add(contact);
			
			this.userRepository.save(user);
			
			System.out.println("DATA"+ contact);
			
			System.out.println("Added  to DataBase");
			
			//add a success messsage 
			session.setAttribute("message", new Message("Your Content is added !!! Add More", "success"));
			
		}catch (Exception e) {
		System.out.println("Error"+ e.getMessage());
		e.printStackTrace();
		//error  message
		session.setAttribute("message", new Message("Something went wrong !!! try Again", "danger"));
		
		}
		
		return "normal/add_contact_form";
	}
	
	//show contacts
	// contact per page =5[n]
	//current page =0 [page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {
		
		m.addAttribute("title", "Show User Contatcs");
		// contact is list bhejni hai  
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		Pageable pageable = PageRequest.of(page, 5);
		
		Page<Contact> contacts = this.contactRepositary.findContactsByUser(user.getId(),pageable);
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());
		
		return "normal/show_contacts";
	}
	// showing particular contact details
	
	@RequestMapping("/{cId}/contact")
	public String showConatcDetails(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		
		System.out.println("CID"+ cId);
		Optional<Contact> contactOption = this.contactRepositary.findById(cId);
		Contact contact = contactOption.get();
		
		String username = principal.getName();
		User user = this.userRepository.getUserByUserName(username);
		
		if(user.getId()== contact.getUser().getId())
		{
		model.addAttribute("contact", contact);
		model.addAttribute("title", contact.getName());
		}
		return "normal/contact_details";
	}
		// delete contact handler 
	@GetMapping("/delete/{cId}")
	public String deleteContact( @PathVariable("cId") Integer cId, Model model, HttpSession httpSession,Principal principal) {
		
		Optional<Contact> contactOptional = this.contactRepositary.findById(cId);
		Contact contact = contactOptional.get();
		//String username = principal.getName();
		
		//delete old photo
		User user = this.userRepository.getUserByUserName(principal.getName());
		user.getContacts().remove(contact);
		this.userRepository.save(user);
		/*
		 * contact.setUser(null);
		 * 
		 * this.contactRepositary.delete(contact);
		 */
		httpSession.setAttribute("message", new Message("Contact Deleted Successfully", "success"));
		System.out.println("Delete contact");
		
		return "redirect:/user/show-contacts/0";
		
	}
	
	//Open update page form handler
	
	@PostMapping("/update-contact/{cId}")
	public String updateForm( @PathVariable("cId") Integer cId ,  Model m ) {
		
		m.addAttribute("title" ,"Update Contact");
		
		Contact contact = this.contactRepositary.findById(cId).get();
		
		m.addAttribute("contact", contact);
		
		return "normal/update_form";
	}
	
	//Update Contact Handler
	@RequestMapping( value="process-update", method = RequestMethod.POST)
	public String updateHanlder(@ModelAttribute Contact contact , @RequestParam("profileImage") MultipartFile file, Model m, HttpSession session , Principal principal) {
		
		try {
			//old contact details
			Contact oldContact = this.contactRepositary.findById(contact.getcId()).get();
			
			//image selected of not 
			if(!file.isEmpty()) {
				//file work update file 
				//delete old photo
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file2 = new File(deleteFile, oldContact.getImage());
				file2.delete();
				//update new photo
				File saveFile = new ClassPathResource("static/img").getFile();
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				contact.setImage(file.getOriginalFilename());
				
				session.setAttribute("message", new Message("Your contact is updated ...", "success"));
			}
			else {
				contact.setImage(oldContact.getImage());
			}
			
		User user = this.userRepository.getUserByUserName(principal.getName());
		contact.setUser(user);
			
			this.contactRepositary.save(contact);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("Contact Name" + contact.getName());
		System.out.println("Contact Email" + contact.getEmail());
		System.out.println("Contact Id" + contact.getcId());
		
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
	//Your Profile Page
	@RequestMapping(value="profile" , method = RequestMethod.GET)
	public String yourProfile(Model m) {
		m.addAttribute("title","Profile Page");
		return "normal/profile";
	}
	
	
	
	
	
	
	
}
