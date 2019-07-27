import { stringify } from 'query-string';
import filterCourses from './filter';
import {
  startGenerating,
  endGenerating,
  filteredCourses,
  startRedirect,
} from '../actions/generator';
import ScheduleMaker from './ScheduleMaker';
import 'whatwg-fetch';
import { shortenUrl } from '../constants/resources';

export const generateSchedules = (values, loadQuery) => (dispatch, getState) => {
  const { courseList } = getState().courses;
  if (courseList.length !== 0) {
    dispatch(startGenerating());
    const filteredResults = filterCourses(values, courseList);
    dispatch(filteredCourses(filteredResults));
    const filteredCourseList = filteredResults.map(result => (
      result.filtered));
    let scheduleMaker = null;
    if (!filteredCourseList.find(list => list.length === 0)) {
      scheduleMaker = new ScheduleMaker(filteredCourseList, parseInt(values.gap, 10));
      try {
        scheduleMaker.makeSchedules();
      } catch (e) {
        scheduleMaker.schedules = e;
      }
    }
    const queryObject = {
      ...values,
      c: courseList.map((course) => {
        const name = `${course[0].subject}${course[0].courseNumber}`;
        return JSON.stringify({
          name,
          ...(values.courses ? values.courses[`${name}${course.id}`] : {}),
        });
      }),
    };
    delete queryObject.courses;
    (loadQuery ? Promise.resolve() : fetch(`${shortenUrl}?${stringify({ data: JSON.stringify(queryObject), prefix: 'generator' })}`))
      .then(response => (loadQuery ? loadQuery.substring(2) : response.json()))
      .then((result) => {
        const querystring = `q=${result}`;
        dispatch(startRedirect({
          pathname: '/generator',
          hash: scheduleMaker.schedules.length > 0 ? '#schedules' : '#timetable',
          search: result ? querystring : '',
        }));
      })
      .catch(() => {
        dispatch(startRedirect({
          pathname: '/generator',
          hash: scheduleMaker.schedules.length > 0 ? '#schedules' : '#timetable',
          search: '',
        }));
      })
      .then(() => {
        console.log(scheduleMaker);
        dispatch(endGenerating(scheduleMaker));
      });
  }
};

export const generateMoreSchedules = () => (dispatch, getState) => {
  dispatch(startGenerating());
  const { scheduleMaker } = getState().generator;
  try {
    scheduleMaker.makeSchedules();
  } catch (e) {
    scheduleMaker.schedules = e;
  }
  console.log(scheduleMaker);
  dispatch(endGenerating(scheduleMaker));
};
