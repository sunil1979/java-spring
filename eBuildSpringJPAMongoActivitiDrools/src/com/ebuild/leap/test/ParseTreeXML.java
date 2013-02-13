package com.ebuild.leap.test;

import java.io.ByteArrayInputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.w3c.dom.Node;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
import com.ebuild.leap.repository.jpa.CategoryRepository;
import com.ebuild.leap.repository.jpa.ElementVariantListRepository;
import com.ebuild.leap.repository.jpa.TypeRepository;
import com.ebuild.leap.repository.mongodb.ElementMongoRepository;
import com.ebuild.leap.util.NullAwareBeanUtilsBean;

import java.sql.Statement;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ebuild-spring-config.xml" })
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class ParseTreeXML {

	protected static Logger log = LoggerFactory.getLogger(ParseTreeXML.class);

	private Connection dbConnection;

	@Autowired
	private CategoryRepository catDao;

	@Autowired
	private TypeRepository typeDao;

	@Autowired
	private ElementVariantListRepository elementVariantListDao;

	@Autowired
	private ElementMongoRepository elementMongoRepository;

	
	public void testNullValidation() throws Exception {
		String s = null;
		validate(s);
	}

	private void validate(Object t) throws Exception {
		if (t == null) {
			System.out.println("Passed Argument is null");
		} else {
			if (t instanceof java.lang.String) {
				Class<? extends Object> tClass = t.getClass();
				if (((java.lang.String) t).length() <= 0) {
					System.out.println("Passed Argument is null");
				} else {
					System.out.println("Passed Arguement is not null");
				}
			}
		}
	}

	public void testBeanUtil() throws Exception {
		Element element1 = new Element();
		element1.setId(new Long("1").longValue());
		element1.setName("ABC");

		Element element2 = new Element();
		element2.setId(new Long("2").longValue());

		NullAwareBeanUtilsBean nullAwareBeanUtil = new NullAwareBeanUtilsBean();
		nullAwareBeanUtil.copyProperties(element1, element2);
		System.out.println("Element1 - Id :" + element1.getId());
		System.out.println("Element1 - Name :" + element1.getName());
	}

	@Test
	public void xmlParseTest() throws Exception {
		String xmlStr = "<E><eid>22631637519267664</eid><mid>22631637519267673</mid><name>IL_0065-AFHW_303_1_BDR1-02-01</name><vlid>22631637519267673</vlid><cat>60</cat><type>10</type><code1>IL_0065-AFHW_303_1_BDR1-02-01</code1><v>0</v><p>0</p><tp>0</tp><qt>1</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>0</cz><zo>0</zo><yo>0</yo><gr>0</gr><dm>null;null;null;</dm><vc>0</vc><vt></vt><E><eid>22631637519183256</eid><mid>22741425858900893</mid><name>Toilet Mirror 1825 CT</name><vlid>22631637519183257</vlid><cat>50</cat><type>20</type><code1>SET-TL01-01-1825-CT</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>1700.0</px><py>20.0</py><pz>0.0</pz><cz>1</cz><zo>5</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>2</vc><vt></vt><E><eid>22631637519186276</eid><mid>22741425858874346</mid><name>Toilet Unit 01 1825 Louver Shutter Common Teak</name><vlid>22631637519186270</vlid><cat>40</cat><type>20</type><code1>TL-SL01_1825_00_CM00_TK</code1><v>0</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>0.0</px><py>0.0</py><pz>189.0</pz><cz>1</cz><zo>1</zo><yo>2</yo><gr>0</gr><dm> 1825.0;582;543;</dm><vc>25</vc><vt>,22741425858882217,</vt><E><eid>22631637519181730</eid><mid>22741425858875189</mid><name>HETTICH PORTERO-1"
				+ "(DETERGENT RACK)</name><vlid>22631637519181733</vlid><cat>20</cat><type>20</type><code1>TO-DRK</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>57</px><py>0</py><pz>460</pz><cz>1</cz><zo>-1</zo><yo>0</yo><gr>0</gr><dm>wg;null;null;</dm><vc>1</vc><vt></vt></E></E><E><eid>22631637519186232</eid><mid>22741425858874347</mid><name>Toilet Mirror 01 1825 Contemporary Teak</name><vlid>22631637519186235</vlid><cat>40</cat><type>20</type><code1>TL-MR01_1825_00_CT00_TK</code1><v>1</v><p>18928</p><tp>18928</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>950</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm> 1825.0;66;1000;</dm><vc>2</vc><vt>,</vt></E></E><E><eid>22631637519198878</eid><mid>22741425858900894</mid><name>Water Closet</name><vlid>22631637519198882</vlid><cat>50</cat><type>40</type><code1>SET-WC_001</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>550.0</px><py>119.1</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>1</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt><E><eid>22631637519199138</eid><mid>22631637519206468</mid><name>Wall1</name><vlid>22631637519199142</vlid><cat>40</cat><type>40</type><code1>wall1</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>0</zo><yo>1</yo><gr>0</gr><dm> 1000.0;null;2400;</dm><vc>1</vc><vt></vt></E><E><eid>22631637519199233</eid><mid>22631637519206464</mid><name>K8752 Odeon  Water Closet</name><vlid>22631637519199237</vlid><cat>40</cat><type>40</type><code1>WC_001</code1><v>1</v><p>5309</p><tp>5309.34</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>300</px><py>0</py><pz>420</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>3</vc><vt>,</vt><E><eid>22631637519198758</eid><mid>22631637519206471</mid><name>K8752-CH Chair bracket for K-8752 (including rubber Bush)</name><vlid>22631637519198766</vlid><cat>30</cat><type>40</type><code1>WC_CB_001</code1><v>1</v><p>1199</p><tp>1198.5</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E></E><E><eid>22631637519199253</eid><mid>22631637519206465</mid><name>573-CP Health/Sanitary Faucet</name><vlid>22631637519199254</vlid><cat>40</cat><type>40</type><code1>WC_HF_002</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>3</vc><vt>,</vt></E><E><eid>22631637519199247</eid><mid>22631637519206466</mid><name>115.770.21.1  Flush Plate</name><vlid>22631637519199243</vlid><cat>40</cat><type>40</type><code1>WC_FV_004</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>5</vc><vt>,</vt><E><eid>22631637519198770</eid><mid>22631637519206477</mid><name>110.72.00.1 Flush Cistern</name><vlid>22631637519198778</vlid><cat>30</cat><type>40</type><code1>WC_FA_002</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E></E><E><eid>22631637519198900</eid><mid>22631637519206467</mid><name> 11053.0</name><vlid>22631637519198888</vlid><cat>40</cat><type>40</type><scat>0</scat><stype>0</stype><code1>ASC_008</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>8</vc><vt>,</vt></E></E><E><eid>22631637519198799</eid><mid>22741425858900895</mid><name>Bath Tub Area with Bath tub and tub filler/mixer</name><vlid>22631637519198802</vlid><cat>50</cat><type>40</type><code1>SET-BT_MX</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>180.0</rt><px>2000.0</px><py>2420.0</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>5</yo><gr>1</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt><E><eid>22631637519199258</eid><mid>22741425858839919</mid><name>Wall2</name><vlid>22631637519199262</vlid><cat>40</cat><type>40</type><code1>wall2</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm> 1000.0;null;2400;</dm><vc>1</vc><vt></vt></E><E><eid>22631637519198950</eid><mid>22741425858839918</mid><name>11105 Jaquar Savoy</name><vlid>22631637519198932</vlid><cat>40</cat><type>40</type><code1>BT_MX_011</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>85</px><py>0</py><pz>750</pz><cz>1</cz><zo>2</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>13</vc><vt>,</vt></E><E><eid>22631637519198921</eid><mid>22741425858839917</mid><name>Lauret Melody Bath Tub</name><vlid>22631637519198905</vlid><cat>40</cat><type>40</type><code1>BT_010</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>10</px><py>0</py><pz>700</pz><cz>1</cz><zo>3</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>13</vc><vt>,</vt><E><eid>22631637519198678</eid><mid>22631637519206245</mid><name>781, Allied, Bathtub drain for Glass pop and melody bath tub</name><vlid>22631637519198683</vlid><cat>30</cat><type>40</type><code1>BT_BA_003</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E></E><E><eid>22631637519199051</eid><mid>22741425858934627</mid><name>28511 Handshower/Hansgrohe </name><vlid>22631637519199033</vlid><cat>40</cat><type>40</type><scat>0</scat><stype>0</stype><code1>SC_HS_011A</code1><v>1</v><p>1265</p><tp>1264.9</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>0.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>0</zo><yo>0</yo><gr>0</gr><dm>null;null;null;</dm><vc>11</vc><vt></vt><E><eid>22631637519198735</eid><mid>22631637519206297</mid><name>28321 Hand Shower Hook</name><vlid>22631637519198740</vlid><cat>30</cat><type>40</type><scat>0</scat><stype>0</stype><code1>SC_HA_007</code1><v>1</v><p>1211</p><tp>1210.52</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E><E><eid>22631637519198738</eid><mid>22631637519206298</mid><name>28276 Hand Shower Hose</name><vlid>22631637519198743</vlid><cat>30</cat><type>40</type><scat>0</scat><stype>0</stype><code1>SC_HA_008</code1><v>1</v><p>1419</p><tp>1418.97</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E></E></E><E><eid>22631637519198823</eid><mid>22741425858900896</mid><name>Normal Shower and Diverter(1way)</name><vlid>22631637519198818</vlid><cat>50</cat><type>40</type><code1>SET-SC_OH_D1</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>-270.0</rt><px>3545.1</px><py>1900.1</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>1</gr><dm>null;null;null;</dm><vc>18</vc><vt></vt><E><eid>22631637519199138</eid><mid>22631637519206341</mid><name>Wall1</name><vlid>22631637519199142</vlid><cat>40</cat><type>40</type><code1>wall1</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm> 1000.0;null;2400;</dm><vc>1</vc><vt></vt></E><E><eid>22631637519199075</eid><mid>22631637519206339</mid><name>28492C Overhead Shower Croma 1 Jet</name><vlid>22631637519199076</vlid><cat>40</cat><type>40</type><code1>SC_OH_002</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>190</px><py>0</py><pz>2300</pz><cz>1</cz><zo>2</zo><yo>1</yo><gr>0</gr><dm> 73.0;null;null;</dm><vc>10</vc><vt>,</vt><E><eid>22631637519198744</eid><mid>22631637519206300</mid><name>487 Overhead Shower Arm Jaquar Allied</name><vlid>22631637519198750</vlid><cat>30</cat><type>40</type><code1>SC_SA_002</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E></E><E><eid>22631637519198974</eid><mid>22631637519206340</mid><name>11139 Mixer and Diverter Jaquar Savoy one way </name><vlid>22631637519198978</vlid><cat>40</cat><type>40</type><code1>SC_D1_001</code1><v>1</v><p>3442</p><tp>3442.13</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>420</px><py>0</py><pz>1300</pz><cz>1</cz><zo>2</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>14</vc><vt>,22631637519198985,22631637519198987,22631637519198989,22631637519198991,22631637519198993,</vt></E></E><E><eid>22631637519198864</eid><mid>22741425858900897</mid><name>Wash Basin - Under Counter</name><vlid>22631637519198865</vlid><cat>50</cat><type>40</type><code1>SET-WB_UC_001</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>2200.0</px><py>50.0</py><pz>0.0</pz><cz>1</cz><zo>3</zo><yo>3</yo><gr>1</gr><dm>null;null;null;</dm><vc>5</vc><vt></vt><E><eid>22631637519199223</eid><mid>22741425858900662</mid><name>769B-CP /Allied</name><vlid>22631637519199226</vlid><cat>40</cat><type>40</type><code1>WB_BT_001</code1><v>1</v><p>1041</p><tp>1040.5</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>2</vc><vt>,</vt></E><E><eid>22631637519198900</eid><mid>22741425858900663</mid><name> 11053.0</name><vlid>22631637519198888</vlid><cat>40</cat><type>40</type><scat>0</scat><stype>0</stype><code1>ASC_008</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>8</vc><vt>,</vt></E><E><eid>22631637519198900</eid><mid>22741425858900664</mid><name> 11053.0</name><vlid>22631637519198888</vlid><cat>40</cat><type>40</type><scat>0</scat><stype>0</stype><code1>ASC_008</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>8</vc><vt>,</vt></E><E><eid>22631637519199228</eid><mid>22741425858900665</mid><name>709 -CP/Allied/ Waste coupling</name><vlid>22631637519199231</vlid><cat>40</cat><type>40</type><code1>WB_WCP_001</code1><v>1</v><p>327</p><tp>326.58</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>2</vc><vt>,</vt></E><E><eid>22631637519199138</eid><mid>22741425858900666</mid><name>Wall1</name><vlid>22631637519199142</vlid><cat>40</cat><type>40</type><code1>wall1</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm> 1000.0;null;2400;</dm><vc>1</vc><vt></vt></E><E><eid>22631637519199168</eid><mid>22741425858900660</mid><name>2215 Wash Basin</name><vlid>22631637519199145</vlid><cat>40</cat><type>40</type><code1>WB_013</code1><v>1</v><p>2660</p><tp>2660.3</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>850</pz><cz>1</cz><zo>2</zo><yo>1</yo><gr>0</gr><dm> 533.0;356;207;</dm><vc>18</vc><vt>22631637519199140,22631637519199143,22631637519199146,22631637519199148,22631637519199150,22631637519199152,22631637519199154,22631637519199156,22631637519199158,22631637519199160,22631637519199166,22631637519199170,22631637519199172,22631637519199174,22631637519199176</vt></E><E><eid>22631637519198965</eid><mid>22741425858900667</mid><name>Counter</name><vlid>22631637519198970</vlid><cat>40</cat><type>40</type><code1>Counter</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>1000</pz><cz>1</cz><zo>3</zo><yo>1</yo><gr>0</gr><dm> 1000.0;null;50;</dm><vc>1</vc><vt></vt></E><E><eid>22741425858920867</eid><mid>22741425858900661</mid><name>11011 B Jaquar Basin Mixer Savoy</name><vlid>22631637519199181</vlid><cat>40</cat><type>40</type><scat>0</scat><stype>0</stype><code1>WB_BM_034</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>440</px><py>0</py><pz>1150</pz><cz>1</cz><zo>4</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>22</vc><vt>22631637519199178,22631637519199180,22631637519199183,22631637519199187,22631637519199199,22631637519199201,22631637519199203,22631637519199205,</vt></E></E><E><eid>22631637519211326</eid><mid>22741425858900898</mid><name>Washbasin Counter</name><vlid>22631637519211328</vlid><cat>50</cat><type>40</type><code1>SET_WB_CT_BTR1_303</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>158.1</px><py>19.1</py><pz>0.0</pz><cz>1</cz><zo>4</zo><yo>2</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E><E><eid>22631637519242211</eid><mid>22741425858900899</mid><name>Window 01 1372</name><vlid>22631637519242215</vlid><cat>50</cat><type>70</type><code1>SET-W01_M</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>-360.0</rt><px>-780.0</px><py>1000.0</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E><E><eid>22631637519238628</eid><mid>22741425858900900</mid><name>Door Left hinged  03 847</name><vlid>22631637519238637</vlid><cat>50</cat><type>70</type><code1>SET-D03_LH</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>-270.0</rt><px>3712.0</px><py>608.1</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt><E><eid>22631637519238727</eid><mid>22631637519239175</mid><name>Door Left hinged 03  847</name><vlid>22631637519238744</vlid><cat>40</cat><type>70</type><code1>D03_LH</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>1</gr><dm> 847.0;null;null;</dm><vc>1</vc><vt></vt></E></E><E><eid>22631637519238689</eid><mid>22741425858900901</mid><name>Window 06 1469</name><vlid>22631637519238703</vlid><cat>50</cat><type>70</type><code1>SET-W06_M</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>2079.1</px><py>2470.0</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E><E><eid>22631637519198807</eid><mid>22741425858900902</mid><name>Cockroach trap</name><vlid>22631637519198812</vlid><cat>50</cat><type>40</type><code1>SET-CTR</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>544.0</px><py>166.1</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt><E><eid>22631637519198963</eid><mid>22631637519206311</mid><name>Cockroach trap with lid</name><vlid>22631637519198967</vlid><cat>40</cat><type>40</type><code1>CTR_001</code1><v>1</v><p>255</p><tp>255.04</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E></E><E><eid>22631637519211295</eid><mid>22741425858900904</mid><name>Shower Cubicle</name><vlid>22631637519211298</vlid><cat>50</cat><type>40</type><code1>SET-SC_CB_BTR1_303</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>2100.0</px><py>830.0</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E><E><eid>22631637519211293</eid><mid>22741425858900905</mid><name>Bath Tub Counter</name><vlid>22631637519211296</vlid><cat>50</cat><type>40</type><code1>SET-BT_CT_BTR1_303</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>158.1</px><py>1434.1</py><pz>0.0</pz><cz>1</cz><zo>2</zo><yo>4</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E><E><eid>22631637519198098</eid><mid>22741425858900906</mid><name>Flooring - Bathroom Wet</name><vlid>22631637519198103</vlid><cat>50</cat><type>50</type><code1>SET-FDC_FL-BTW</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>0.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>2</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt><E><eid>22631637519198420</eid><mid>22631637519206133</mid><name>Botticino</name><vlid>22631637519198423</vlid><cat>40</cat><type>50</type><code1>FL01</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>0</zo><yo>0</yo><gr>0</gr><dm>null;null;null;</dm><vc>78</vc><vt>,22631637519198425,22631637519198427,22631637519198433,22631637519198439,22631637519198441,22631637519198443,22631637519198445,22631637519198447,22631637519198449,22631637519198451,22631637519198453,22631637519198455,22631637519198457,22631637519198459,22631637519198461,22631637519198463,22631637519198465,22631637519198467,22631637519198469,22631637519198473,22631637519198477,22631637519198481,22631637519198483,22631637519198485,22631637519198491,22631637519198501,22631637519198503,22631637519198505,22631637519198507,22631637519198509,22631637519198511,22631637519198513,22631637519198515,22631637519198543,22631637519198545,22631637519198547,22631637519198551,22631637519198553,22631637519198555,22631637519198557,22631637519198559,22631637519198561,22631637519198563,22631637519198565,22631637519198567,22631637519198569,22631637519198571,22631637519198573,22631637519198575</vt></E></E><E><eid>22631637519198080</eid><mid>22741425858900907</mid><name>Dado - Bathroom</name><vlid>22631637519198085</vlid><cat>50</cat><type>50</type><code1>SET-FDC_DD-BTR</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>1500.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>2</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt><E><eid>22631637519198231</eid><mid>22631637519206127</mid><name>Botticino</name><vlid>22631637519198234</vlid><cat>40</cat><type>50</type><code1>DD01</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>0</zo><yo>0</yo><gr>0</gr><dm>null;null;null;</dm><vc>94</vc><vt>,22631637519198242,22631637519198244,22631637519198246,22631637519198248,22631637519198250,22631637519198254,22631637519198258,22631637519198260,22631637519198262,22631637519198264,22631637519198266,22631637519198268,22631637519198270,22631637519198272,22631637519198274,22631637519198278,22631637519198280,22631637519198282,22631637519198284,22631637519198286,22631637519198288,22631637519198298,22631637519198304,22631637519198306,22631637519198308,22631637519198310,22631637519198312,22631637519198314,22631637519198316,22631637519198362,22631637519198364,22631637519198370,22631637519198374,22631637519198376,22631637519198378,22631637519198380,22631637519198382,22631637519198394,22631637519198396,22631637519198398,22631637519198400,22631637519198402,22631637519198404,22631637519198406,22631637519198408,22631637519198410,22631637519198412,22631637519198414,22631637519198416,22631637519198418</vt></E></E><E><eid>22631637519198116</eid><mid>22741425858900908</mid><name>Counter - Tub</name><vlid>22631637519198121</vlid><cat>50</cat><type>50</type><code1>SET-FDC_FL-TUB</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>4500.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>2</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt><E><eid>22631637519198420</eid><mid>22631637519206139</mid><name>Botticino</name><vlid>22631637519198423</vlid><cat>40</cat><type>50</type><code1>FL01</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>0</zo><yo>0</yo><gr>0</gr><dm>null;null;null;</dm><vc>78</vc><vt>,22631637519198425,22631637519198427,22631637519198433,22631637519198439,22631637519198441,22631637519198443,22631637519198445,22631637519198447,22631637519198449,22631637519198451,22631637519198453,22631637519198455,22631637519198457,22631637519198459,22631637519198461,22631637519198463,22631637519198465,22631637519198467,22631637519198469,22631637519198473,22631637519198475,22631637519198477,22631637519198479,22631637519198481,22631637519198483,22631637519198485,22631637519198487,22631637519198489,22631637519198491,22631637519198493,22631637519198495,22631637519198497,22631637519198499,22631637519198501,22631637519198503,22631637519198505,22631637519198507,22631637519198509,22631637519198511,22631637519198513,22631637519198515,22631637519198543,22631637519198545,22631637519198547,22631637519198549,22631637519198551,22631637519198553,22631637519198555,22631637519198557,22631637519198559,22631637519198561,22631637519198563,22631637519198565,22631637519198567,22631637519198569,22631637519198571,22631637519198573,22631637519198575</vt></E></E><E><eid>22631637519198086</eid><mid>22741425858900909</mid><name>Dado - Tub</name><vlid>22631637519198091</vlid><cat>50</cat><type>50</type><code1>SET-FDC_DD-TUB</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>6000.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>2</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt><E><eid>22631637519198231</eid><mid>22631637519206129</mid><name>Botticino</name><vlid>22631637519198234</vlid><cat>40</cat><type>50</type><code1>DD01</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>0</zo><yo>0</yo><gr>0</gr><dm>null;null;null;</dm><vc>94</vc><vt></vt></E></E><E><eid>22631637519198068</eid><mid>22741425858900910</mid><name>Counter - Bathroom</name><vlid>22631637519198072</vlid><cat>50</cat><type>50</type><code1>SET-FDC_CT-BTR</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>3000.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>2</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt><E><eid>22631637519198178</eid><mid>22631637519206123</mid><name>White Dupont Corian Counter</name><vlid>22631637519198181</vlid><cat>40</cat><type>50</type><code1>CT01</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>0</zo><yo>0</yo><gr>0</gr><dm>null;null;null;</dm><vc>26</vc><vt>,22631637519198180,22631637519198183,22631637519198185,22631637519198187,22631637519198189,22631637519198207,22631637519198213,22631637519198215,22631637519198219,22631637519198227,22631637519198229</vt></E></E><E><eid>22631637519198797</eid><mid>22741425858922921</mid><name>Towel rack, Towel ring, Toilet paper holder, Soap dish, Soap dish Ceramic</name><vlid>22631637519198790</vlid><cat>50</cat><type>40</type><code1>SET-TRK_TRG_TPH_SPD_SPD-CR</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>0.0</px><py>3000.0</py><pz>0.0</pz><cz>1</cz><zo>0</zo><yo>0</yo><gr>0</gr><dm>null;null;null;</dm><vc>7</vc><vt></vt><E><eid>22631637519198662</eid><mid>22741425858920875</mid><name>1181  Towel rack</name><vlid>22631637519198658</vlid><cat>20</cat><type>40</type><scat>0</scat><stype>0</stype><code1>PS_TRK_004</code1><v>0</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>0.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>-1</zo><yo>-1</yo><gr>1</gr><dm>null;null;null;</dm><vc>5</vc><vt></vt></E><E><eid>22631637519198648</eid><mid>22741425858920876</mid><name>Towel ring</name><vlid>22631637519198651</vlid><cat>20</cat><type>40</type><code1>PS_TRG_001</code1><v>1</v><p>805</p><tp>805.38</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>0.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>-1</zo><yo>-1</yo><gr>2</gr><dm>null;null;null;</dm><vc>4</vc><vt></vt></E><E><eid>22631637519198641</eid><mid>22741425858920877</mid><name>Toilet paper holder 1151</name><vlid>22631637519198644</vlid><cat>20</cat><type>40</type><code1>PS_TPH_001</code1><v>1</v><p>592</p><tp>591.63</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>0.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>-1</zo><yo>-1</yo><gr>3</gr><dm>null;null;null;</dm><vc>4</vc><vt></vt></E><E><eid>22631637519198633</eid><mid>22741425858920878</mid><name>Soap dish</name><vlid>22631637519198637</vlid><cat>20</cat><type>40</type><code1>PS_SPD_001</code1><v>1</v><p>528</p><tp>527.5</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>0.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>-1</zo><yo>-1</yo><gr>4</gr><dm>null;null;null;</dm><vc>3</vc><vt></vt></E><E><eid>22631637519198631</eid><mid>22741425858920879</mid><name>Soap dish</name><vlid>22631637519198634</vlid><cat>20</cat><type>40</type><code1>PS_SPD-CR_001</code1><v>1</v><p>659</p><tp>658.6</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>0.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>-1</zo><yo>-1</yo><gr>5</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E></E><E><eid>22631637519198810</eid><mid>22741425858937980</mid><name>Linear Drain</name><vlid>22631637519198815</vlid><cat>50</cat><type>40</type><scat>0</scat><stype>0</stype><code1>SET-DR</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>-270.0</rt><px>3449.1</px><py>1652.1</py><pz>0.0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt><E><eid>22741425858920822</eid><mid>22741425858937802</mid><name>ACO Drains 92301 Stainless Steel</name><vlid>22741425858920825</vlid><cat>40</cat><type>40</type><scat>0</scat><stype>0</stype><code1>DR_001</code1><v>1</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>1</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt></E></E><E><eid>22631637519198804</eid><mid>22741425858939954</mid><name>Concealed Stop Cock</name><vlid>22631637519198809</vlid><cat>50</cat><type>40</type><scat>0</scat><stype>0</stype><code1>SET-CSC-CM</code1><v>1</v><p>0</p><tp>0</tp><qt>1.0</qt><sc>1.0,1.0</sc><rt>0.0</rt><px>0.0</px><py>0.0</py><pz>0.0</pz><cz>1</cz><zo>0</zo><yo>0</yo><gr>0</gr><dm>null;null;null;</dm><vc>1</vc><vt></vt><E><eid>22631637519198958</eid><mid>22741425858937799</mid><name>11083 / Jaguar Savoy</name><vlid>22631637519198959</vlid><cat>40</cat><type>40</type><scat>0</scat><stype>0</stype><code1>CSC_002</code1><v>0</v><p>0</p><tp>0</tp><qt>1.00</qt><sc>1,1</sc><rt>0</rt><px>0</px><py>0</py><pz>0</pz><cz>1</cz><zo>1</zo><yo>1</yo><gr>1</gr><dm>null;null;null;</dm><vc>4</vc><vt>,22741425858920820</vt></E></E></E>";
		this.getConnection();
		Statement stmt = dbConnection.createStatement();
		ResultSet rst = stmt.executeQuery("SELECT * FROM TREE");
		while (rst.next()) {
			xmlStr = rst.getString("xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new ByteArrayInputStream(xmlStr.getBytes())));
			ElementManifest elementManifest = new ElementManifest();
			elementManifest = recurseElementDocument(elementManifest, doc.getDocumentElement());
			elementMongoRepository.save(elementManifest.getChildElement());
		}
	}

	public ElementManifest recurseElementDocument(ElementManifest em, Node node) {
		if (node.getNodeName().equalsIgnoreCase("E")) {
			System.out.println("Element Node Found");
			// Create Element object
			Element element = new Element();
			element.setActiveStatus(true);
			NodeList nodeList = node.getChildNodes();
			boolean elementEncounterd = false;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node currentNode = nodeList.item(i);
				Node currentNodeValueNode = currentNode.getFirstChild();
				if (currentNode.getNodeName().equalsIgnoreCase("eid")) {
					element.setId(new Long(currentNodeValueNode.getNodeValue()).longValue());
				}
				if (currentNode.getNodeName().equalsIgnoreCase("cat") && currentNode.getNodeValue() != null) {
					Category cat = catDao.findOne(new Long(currentNodeValueNode.getNodeValue()).longValue());
					element.setCategory(cat);
				}
				if (currentNode.getNodeName().equalsIgnoreCase("type")) {
					Type type = typeDao.findOne(new Long(currentNodeValueNode.getNodeValue()).longValue());
					element.setType(type);
				}
				if (currentNode.getNodeName().equalsIgnoreCase("code1")) {
					element.setCode1(currentNodeValueNode.getNodeValue());
				}
				if (currentNode.getNodeName().equalsIgnoreCase("vlid") && currentNodeValueNode.getNodeValue() != null) {
					ElementVariantList evList = elementVariantListDao.findOne(new Long(currentNodeValueNode.getNodeValue()).longValue());
					element.setElementVariantList(evList);
				}
				if (currentNode.getNodeName().equalsIgnoreCase("mid")) {
					em.setId(new Long(currentNodeValueNode.getNodeValue()).longValue());
				}
				if (currentNode.getNodeName().equalsIgnoreCase("cz")) {
					if (currentNodeValueNode.getNodeValue().equalsIgnoreCase("1")) {
						em.setCustomize(true);
					} else {
						em.setCustomize(false);
					}
				}
				if (currentNode.getNodeName().equalsIgnoreCase("zo")) {
					if (currentNodeValueNode.getNodeValue().equalsIgnoreCase("1")) {
						em.setzOrder(true);
					} else {
						em.setzOrder(false);
					}
				}
				if (currentNode.getNodeName().equalsIgnoreCase("yo")) {
					if (currentNodeValueNode.getNodeValue().equalsIgnoreCase("1")) {
						em.setyOrder(true);
					} else {
						em.setyOrder(false);
					}
				}
				if (currentNode.getNodeName().equalsIgnoreCase("qt")) {
					em.setQuantity(new Double(currentNodeValueNode.getNodeValue()).doubleValue());
				}
				if (currentNode.getNodeName().equalsIgnoreCase("sc")) {
					StringTokenizer st1 = new StringTokenizer(currentNodeValueNode.getNodeValue(), ",");
					while (st1.hasMoreTokens()) {
						em.setScale(new Double(st1.nextToken()).doubleValue());
						em.setYscale(new Double(st1.nextToken()).doubleValue());
					}
				}
				if (currentNode.getNodeName().equalsIgnoreCase("rt")) {
					em.setRotate(new Double(currentNodeValueNode.getNodeValue()).doubleValue());
				}
				if (currentNode.getNodeName().equalsIgnoreCase("px")) {
					em.setPositionX(new Double(currentNodeValueNode.getNodeValue()).doubleValue());
				}
				if (currentNode.getNodeName().equalsIgnoreCase("py")) {
					em.setPositionY(new Double(currentNodeValueNode.getNodeValue()).doubleValue());
				}
				if (currentNode.getNodeName().equalsIgnoreCase("pz")) {
					em.setPositionZ(new Double(currentNodeValueNode.getNodeValue()).doubleValue());
				}
				if (currentNode.getNodeName().equalsIgnoreCase("gr")) {
					em.setElementGroup(new Integer(currentNodeValueNode.getNodeValue()).intValue());
				}
				if (currentNode.getNodeName().equalsIgnoreCase("p")) {
					em.setPrime(new Integer(currentNodeValueNode.getNodeValue()).intValue());
				}
				if (currentNode.getNodeName().equalsIgnoreCase("E")) {
					elementEncounterd = true;
					ElementManifest newElementManifest = new ElementManifest();
					newElementManifest = recurseElementDocument(newElementManifest, currentNode);
					element.addElementManifest(newElementManifest);
					em.setChildElement(element);
					// Find Parent Node and Add ElementManifest
				}
			}
			if (!elementEncounterd) {
				em.setChildElement(element);
			}
		}
		return em;
	}

	public void recurseDocument(Node node) {
		// do something with the current node instead of System.out
		System.out.println(node.getNodeName());
		NodeList list = node.getChildNodes();

		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			Node firstChild = currentNode.getFirstChild();
			System.out.println("First Child :" + firstChild.getNodeValue());
			if (currentNode.getNodeType() == Node.ELEMENT_NODE && currentNode.getNodeName().equalsIgnoreCase("E")) {
				// calls this method for all the children which is Element
				recurseDocument(currentNode);
			}
		}
	}

	private void getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ebuild_afhw", "root", "root");
	}

	private void closeConnection() throws Exception {
		if (dbConnection != null)
			dbConnection.close();
	}
}
