package net.lzzy.practicesonline.activities.nework;

import net.lzzy.practicesonline.activities.models.Question;
import net.lzzy.practicesonline.activities.models.view.QuestionType;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by lzzy_gxy on 2019/4/23.
 * Description:
 */
public class QuestionServiceTest {

    @Test
    public void testGetQuestionsOfPracticeFromServer() throws IOException {
        String actual= QuestionService.getQuestionsOfPracticeFromServer(28);
        assertTrue(actual.contains("在网上"));
    }
    @Test
    public void testetQuestions() throws IOException, IllegalAccessException, JSONException, InstantiationException {
        String json=QuestionService.getQuestionsOfPracticeFromServer(28);
        List<Question> questions=QuestionService.getQuestions(json, UUID.randomUUID());
        assertEquals(6,questions.size());
        Question question=questions.get(1);
        assertTrue(question.getContent().contains("研究和分析市场需求情况"));
        assertEquals(QuestionType.SINGLE_CHOICE,question.getTupe());
        assertEquals(4,question.getOptions().size());
        assertTrue(question.getOptions().get(0).isAnswer());
    }

}