package com.ebuild.leap.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ebuild.leap.drools.LookupPaletteUtil;
import com.ebuild.leap.pojo.Brand;
import com.ebuild.leap.pojo.CPR;
import com.ebuild.leap.pojo.Category;
import com.ebuild.leap.pojo.CostVersion;
import com.ebuild.leap.pojo.Element;
import com.ebuild.leap.pojo.ElementManifest;
import com.ebuild.leap.pojo.ElementVariantList;
import com.ebuild.leap.pojo.Finish;
import com.ebuild.leap.pojo.HomeUnit;
import com.ebuild.leap.pojo.HomeUnitRevision;
import com.ebuild.leap.pojo.HomeUnitVersion;
import com.ebuild.leap.pojo.Material;
import com.ebuild.leap.pojo.Product;
import com.ebuild.leap.pojo.Project;
import com.ebuild.leap.pojo.SubType;
import com.ebuild.leap.pojo.Theme;
import com.ebuild.leap.pojo.Type;
import com.ebuild.leap.pojo.User;

public class EbuildleapUtil {

	protected static Logger log = LoggerFactory.getLogger(EbuildleapUtil.class);

	private Properties props = null;
	private Session session;
	private String smtpUserName;
	private String smtpPassword;
	private String mailFrom;

	@Autowired
	private LookupPaletteUtil lookupPaletteUtil;

	public EbuildleapUtil() {
	}

	private Session getSMTPSession() {
		if (this.session == null) {
			this.session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(smtpUserName, smtpPassword);
				}
			});
		}
		return this.session;

	}

	public void setProps(Properties props) {
		log.debug("SET PROPERTIES CALLED :" + props);
		this.props = props;
	}

	public void setSmtpUserName(String smtpUserName) {
		log.debug("SET smtpUserName CALLED :" + smtpUserName);
		this.smtpUserName = smtpUserName;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public <T> List<T> getUniqueItems(List<T> ListA, List<T> ListB) {
		List<T> retList = new ArrayList<T>();
		for (T t : ListA) {
			if (!ListB.contains(t)) {
				retList.add(t);
			}
		}

		for (T t : ListB) {
			if (!ListA.contains(t)) {
				retList.add(t);
			}
		}
		return retList;
	}

	public List<Element> getUniqueElementList(List<Element> ListA, List<Element> ListB) {
		for (Element eA : ListA) {
			Boolean found = false;
			for (Element eB : ListB) {
				if (eA.getId().equals(eB.getId())) {
					found = true;
					break;
				}
			}
			if (!found) {
				ListB.add(eA);
			}
		}
		return ListB;
	}

	public void sendMail(String subject, String body, ArrayList<String> toEmailAddr) {
		for (String toAddr : toEmailAddr) {
			try {
				Message message = new MimeMessage(this.getSMTPSession());
				message.setFrom(new InternetAddress(mailFrom));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddr));
				message.setSubject(subject);
				message.setText(body);
				Transport.send(message);
			} catch (MessagingException e) {
				log.debug("EXCEPTION SENDING EMAIL :" + toAddr);
				log.debug(e.getClass() + ": " + e.getMessage(), e);
			}
		}
	}

	public Boolean isNull(Object t) {
		Boolean validateResult = false;
		if (t == null) {
			validateResult = true;
		} else {
			if (t instanceof java.lang.String) {
				if (((java.lang.String) t).length() <= 0) {
					validateResult = true;
				}
			}
			if (t instanceof Brand) {
				if (((Brand) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof Category) {
				if (((Category) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof CostVersion) {
				if (((CostVersion) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof CPR) {
				if (((CPR) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof Element) {
				if (((Element) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof ElementManifest) {
				if (((ElementManifest) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof ElementVariantList) {
				if (((ElementVariantList) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof Finish) {
				if (((Finish) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof HomeUnit) {
				if (((HomeUnit) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof HomeUnitRevision) {
				if (((HomeUnitRevision) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof HomeUnitVersion) {
				if (((HomeUnitVersion) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof Material) {
				if (((Material) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof Product) {
				if (((Product) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof Project) {
				if (((Project) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof SubType) {
				if (((SubType) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof Theme) {
				if (((Theme) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof Type) {
				if (((Type) t).getId() == null) {
					validateResult = true;
				}
			}

			if (t instanceof User) {
				if (((User) t).getId() == null) {
					validateResult = true;
				}
			}

		}
		return validateResult;
	}

}
