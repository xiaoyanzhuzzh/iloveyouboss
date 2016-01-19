package iloveyouboss;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

public class ProfileTest {

    private Criterion mockedCriterion1;
    private Criterion mockedCriterion2;
    private Profile profile;
    private Criteria criteria;
    private Answer mockedAnswer1;
    private Answer mockedAnswer2;

    @Before
    public void setUp() throws Exception {

        profile = new Profile("fake name");
        criteria = new Criteria();

        mockedAnswer1 = mock(Answer.class);
        when(mockedAnswer1.getQuestionText()).thenReturn("question1?");
        profile.add(mockedAnswer1);

        mockedAnswer2 = mock(Answer.class);
        when(mockedAnswer2.getQuestionText()).thenReturn("question2?");
        profile.add(mockedAnswer2);

        mockedCriterion1 = mock(Criterion.class);
        when(mockedCriterion1.getAnswer()).thenReturn(mockedAnswer1);

        mockedCriterion2 = mock(Criterion.class);
        when(mockedCriterion2.getAnswer()).thenReturn(mockedAnswer2);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void killedWhenCriteriaAllAreMustMatchAndNoMath() {
        when(mockedCriterion1.getWeight()).thenReturn(Weight.MustMatch);
        when(mockedAnswer1.match(any(Answer.class))).thenReturn(false);
        criteria.add(mockedCriterion1);

        boolean matches = profile.matches(criteria);
        assertThat(matches, equalTo(false));
    }

    @Test
    public void notKilledAndAnyMatchedWhenOneOfCriteriaIsDoNotCare() {
        when(mockedCriterion1.getWeight()).thenReturn(Weight.DontCare);
        when(mockedAnswer1.match(any(Answer.class))).thenReturn(false);

        when(mockedCriterion2.getWeight()).thenReturn(Weight.WouldPrefer);
        when(mockedAnswer2.match(any(Answer.class))).thenReturn(false);

        criteria.add(mockedCriterion1);
        criteria.add(mockedCriterion2);

        boolean matches = profile.matches(criteria);
        assertThat(matches, equalTo(true));
    }

    @Test
    public void notKilledAndNothingMatchedWhen() {

        when(mockedCriterion1.getWeight()).thenReturn(Weight.WouldPrefer);
        when(mockedAnswer1.match(any(Answer.class))).thenReturn(false);
        criteria.add(mockedCriterion1);

        boolean matches = profile.matches(criteria);
        assertThat(matches, equalTo(false));
    }

    @Test
    public void scoreStayedWhen() {

    }

    @Test
    public void scoreIncreasedWhen() {

    }

    @Test
    public void returnTrueAndScoreIs0WhenCriteriaAllDoNotCare() {

        when(mockedCriterion1.getWeight()).thenReturn(Weight.DontCare);
        when(mockedCriterion2.getWeight()).thenReturn(Weight.DontCare);

        criteria.add(mockedCriterion1);
        criteria.add(mockedCriterion2);

        Boolean isMatched = profile.matches(criteria);
        assertThat(profile.score(), equalTo(0));
        assertThat(isMatched, equalTo(true));
    }

    @Test
    public void returnFalseAndScoreIs0WhenOneOfCriteriaIsDoNotCare() {

        when(mockedCriterion1.getWeight()).thenReturn(Weight.DontCare);
        when(mockedCriterion2.getWeight()).thenReturn(Weight.MustMatch);
        when(mockedAnswer2.match(any(Answer.class))).thenReturn(false);

        criteria.add(mockedCriterion1);
        criteria.add(mockedCriterion2);

        Boolean isMatched = profile.matches(criteria);
        assertThat(profile.score(), equalTo(0));
        assertThat(isMatched, equalTo(false));
    }

    @Test
    public void returnTrueAndScoreIsMaxWhenOneOfCriteriaMustMatchAndNotMatch() {

        when(mockedCriterion1.getWeight()).thenReturn(Weight.DontCare);
        when(mockedCriterion2.getWeight()).thenReturn(Weight.MustMatch);
        when(mockedAnswer2.match(any(Answer.class))).thenReturn(true);

        criteria.add(mockedCriterion1);
        criteria.add(mockedCriterion2);

        Boolean isMatched = profile.matches(criteria);
        assertThat(profile.score(), equalTo(2147483647));
        assertThat(isMatched, equalTo(true));
    }

    @Test
    public void returnFalseAndScoreIs0WhenCriteriaAllAreMustMatchAndNoOneMath() {

        when(mockedCriterion1.getWeight()).thenReturn(Weight.MustMatch);
        when(mockedAnswer1.match(any(Answer.class))).thenReturn(false);

        when(mockedCriterion2.getWeight()).thenReturn(Weight.MustMatch);
        when(mockedAnswer2.match(any(Answer.class))).thenReturn(false);

        criteria.add(mockedCriterion1);
        criteria.add(mockedCriterion2);

        Boolean isMatched = profile.matches(criteria);
        assertThat(profile.score(), equalTo(0));
        assertThat(isMatched, equalTo(false));
    }

    @Test
    public void returnFalseAndScoreIsMaxWhenCriteriaAllAreMustMatchAndOneNotMath() {

        when(mockedCriterion1.getWeight()).thenReturn(Weight.MustMatch);
        when(mockedAnswer1.match(any(Answer.class))).thenReturn(true);

        when(mockedCriterion2.getWeight()).thenReturn(Weight.MustMatch);
        when(mockedAnswer2.match(any(Answer.class))).thenReturn(false);

        criteria.add(mockedCriterion1);
        criteria.add(mockedCriterion2);

        Boolean isMatched = profile.matches(criteria);
        assertThat(profile.score(), equalTo(2147483647));
        assertThat(isMatched, equalTo(false));
    }

    @Test
    public void returnTrueAndScoreIs0WhenCriteriaAllAreMustMatchAndAllMath() {

        when(mockedCriterion1.getWeight()).thenReturn(Weight.MustMatch);
        when(mockedAnswer1.match(any(Answer.class))).thenReturn(true);

        when(mockedCriterion2.getWeight()).thenReturn(Weight.MustMatch);
        when(mockedAnswer2.match(any(Answer.class))).thenReturn(true);

        criteria.add(mockedCriterion1);
        criteria.add(mockedCriterion2);

        Boolean isMatched = profile.matches(criteria);
        assertThat(profile.score(), equalTo(-2));
        assertThat(isMatched, equalTo(true));
    }

    @Test
    public void returnTrueAndScoreIs200WhenCriteriaAllMathWouldPrefer() {

        when(mockedCriterion1.getWeight()).thenReturn(Weight.WouldPrefer);
        when(mockedAnswer1.match(any(Answer.class))).thenReturn(true);

        when(mockedCriterion2.getWeight()).thenReturn(Weight.WouldPrefer);
        when(mockedAnswer2.match(any(Answer.class))).thenReturn(true);

        criteria.add(mockedCriterion1);
        criteria.add(mockedCriterion2);

        Boolean isMatched = profile.matches(criteria);
        assertThat(profile.score(), equalTo(200));
        assertThat(isMatched, equalTo(true));
    }
}