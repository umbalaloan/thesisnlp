package thesis.nlp.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import thesis.nlp.core.process.export.tuple.TupleExportProcessing;
import thesis.nlp.core.process.sentence.ClauseProcessing;
import thesis.nlp.core.process.sentence.SentenceProcessing;
import thesis.nlp.keywords.KeywordsExtraction;
import thesis.nlp.models.Clause;
import thesis.nlp.models.Sentence;
import thesis.nlp.models.Tuple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TypedDependency;

public class Main extends JFrame {

	private JPanel contentPane;
	private String inputsentence = "";
//	private JTextArea textAreainputSentence;
//	private JButton btnParse;
//	private JButton btnParseOllie;
//	private JButton btnParseClausie;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setResizable(false);
		setTitle("Thesis Demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1043, 797);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnNewMenu.add(mntmExit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		
		JPanel StanfordParser = new JPanel();
		StanfordParser.setBorder(new TitledBorder(null, "Stanford Parser", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel pannel_new = new JPanel();
		pannel_new.setBorder(new TitledBorder(null, "New Algorithm", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel lblKeywords = new JLabel("Keywords");
		
		JScrollPane scrollPane_4 = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblKeywords)
								.addComponent(StanfordParser, GroupLayout.PREFERRED_SIZE, 434, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane_4, GroupLayout.PREFERRED_SIZE, 439, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(pannel_new, GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(StanfordParser, GroupLayout.PREFERRED_SIZE, 427, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblKeywords)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane_4, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE))
						.addComponent(pannel_new, GroupLayout.PREFERRED_SIZE, 677, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		JTextArea textAreaKeywords = new JTextArea();
		scrollPane_4.setViewportView(textAreaKeywords);
		JButton btnParse = new JButton("Parse");
		
		JLabel lblTripleExtraction = new JLabel("Triple Extraction");
		
		JLabel lblSyntaxModel = new JLabel("Syntax Model by Json");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_pannel_new = new GroupLayout(pannel_new);
		gl_pannel_new.setHorizontalGroup(
			gl_pannel_new.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pannel_new.createSequentialGroup()
					.addGroup(gl_pannel_new.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblTripleExtraction)
						.addComponent(lblSyntaxModel)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
						.addComponent(scrollPane))
					.addContainerGap(18, Short.MAX_VALUE))
		);
		gl_pannel_new.setVerticalGroup(
			gl_pannel_new.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pannel_new.createSequentialGroup()
					.addGap(8)
					.addComponent(lblTripleExtraction)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(lblSyntaxModel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 338, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		JTextArea textAreaSyntaxModel = new JTextArea();
		textAreaSyntaxModel.setEditable(false);
		scrollPane.setViewportView(textAreaSyntaxModel);
		
		JTextArea textAreaTriples = new JTextArea();
		scrollPane_1.setViewportView(textAreaTriples);
		pannel_new.setLayout(gl_pannel_new);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		GroupLayout gl_StanfordParser = new GroupLayout(StanfordParser);
		gl_StanfordParser.setHorizontalGroup(
			gl_StanfordParser.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_StanfordParser.createSequentialGroup()
					.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 419, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_StanfordParser.setVerticalGroup(
			gl_StanfordParser.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
		);
		
		JTextArea textAreaTypedDependency = new JTextArea();
		textAreaTypedDependency.setEditable(false);
		scrollPane_2.setViewportView(textAreaTypedDependency);
		StanfordParser.setLayout(gl_StanfordParser);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		
		JButton btnParseParagraph = new JButton("Parse Paragraph");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(3)
					.addComponent(scrollPane_3)
					.addGap(18)
					.addComponent(btnParse, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnParseParagraph)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(15)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnParse, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
								.addComponent(btnParseParagraph, GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)))
						.addComponent(scrollPane_3, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		JTextArea textAreainputSentence = new JTextArea();
		textAreainputSentence.setFont(new Font("MS Mincho", Font.PLAIN, 12));
		textAreainputSentence.setLineWrap(true);
		textAreainputSentence.setText("Enter your sentence here");
		scrollPane_3.setViewportView(textAreainputSentence);
		panel.setLayout(gl_panel);
		btnParse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				inputsentence = textAreainputSentence.getText().trim();
				ClauseProcessing cp = new ClauseProcessing();
				cp.parseTree(inputsentence);
				textAreaTypedDependency.setText(treeToString(cp.parse, new TreePrint("penn"), cp.tdls));
				SentenceProcessing sspi = new SentenceProcessing();
				ObjectMapper mapper = new ObjectMapper();
				String json ="";
				Sentence senprocess = sspi.processSentence(inputsentence);
				Gson gson = new Gson();
				JsonParser p = new JsonParser();
				KeywordsExtraction keywordextract = new KeywordsExtraction();
				
				try {
					json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(senprocess);
					textAreaSyntaxModel.setText(json);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Set<String> keywords = keywordextract.extractKeyWords("headNoun", p.parse(json));
				textAreaKeywords.setText(listKeywordsToString(keywords));
				List<Clause> clauses = senprocess.getClauses();
				textAreaTriples.setText(listTuples(clauses));
			}
		});
		btnParseParagraph.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// input paragraph
				inputsentence = textAreainputSentence.getText().trim();
				Reader reader = new StringReader(inputsentence);
				DocumentPreprocessor dp = new DocumentPreprocessor(reader);
				List<String> sentenceList = new ArrayList<String>();
				KeywordsExtraction keywordextract = new KeywordsExtraction();
				for (List<HasWord> sentence : dp) {
				   String sentenceString = edu.stanford.nlp.ling.Sentence.listToString(sentence);
				   sentenceList.add(sentenceString.toString());
				}
				List<Clause> clauses = new ArrayList<Clause>();
				Set<String> keywords = new HashSet<String>();
				Gson gson = new Gson();
				JsonParser p = new JsonParser();
				for (String sentence : sentenceList)
				{
					SentenceProcessing sspi = new SentenceProcessing();
					Sentence senprocess = sspi.processSentence(sentence);
					clauses.addAll(senprocess.getClauses());
					String json = gson.toJson(senprocess);
					keywords.addAll(keywordextract.extractKeyWords("headNoun", p.parse(json)));
				}
				textAreaTriples.setText(listTuples(clauses));
				textAreaKeywords.setText(listKeywordsToString(keywords));
				textAreaTypedDependency.setText("");
				textAreaSyntaxModel.setText("");
			}
		});
		contentPane.setLayout(gl_contentPane);
	}
	
	private static String treeToString(Tree t, TreePrint tp, List<TypedDependency> tdls) {
		  StringWriter sw = new StringWriter();
		  tp.printTree(t, (new PrintWriter(sw)));
		  StringBuilder printout = new StringBuilder();
		  printout.append(sw.toString());
		  printout.append("\n");
		  for (TypedDependency tdl: tdls)
			  printout.append(tdl.toString() + "\n");
		  return printout.toString();
		}
	private static String listTuples(List<Clause> clauses)
	{
		StringBuilder output = new StringBuilder();
		for (Clause clause : clauses) {
			TupleExportProcessing tup = new TupleExportProcessing();
			Set<Tuple> tuples = new LinkedHashSet<Tuple>();
			tuples = tup.exportTuplesOfClause(clause);
			Iterator<Tuple> itr = tuples.iterator();
			while (itr.hasNext()) {
				output.append(itr.next().toString());
				output.append("\n");
			}	
		}
		return output.toString();
	}
	
	private static String listKeywordsToString(Set<String> keywords)
	{
		StringBuilder output = new StringBuilder();
		Iterator<String> itrKeyword = keywords.iterator();
		while (itrKeyword.hasNext()) {
			output.append(itrKeyword.next().toString());
			output.append("\n");
			
		}
		return output.toString();
	}
}
