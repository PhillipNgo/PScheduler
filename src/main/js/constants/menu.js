import Generator from '../containers/content/Generator';
import Builder from '../containers/content/Builder';
import Timetable from '../components/content/Timetable';
import About from '../components/content/About';
import SearchForm from '../containers/content/generator/SearchForm';
import Schedules from '../containers/content/generator/Schedules';

export default [{
  path: '/generator',
  title: 'GENERATOR',
  subMenu: [{
    hash: '#search',
    title: 'Modify Search',
    component: SearchForm,
  }, {
    hash: '#schedules',
    title: 'Schedules',
    component: Schedules,
  }, {
    hash: '#timetable',
    title: 'Timetable',
  }],
  component: Generator,
}, {
  path: '/builder',
  title: 'BUILDER',
  subMenu: [],
  component: Builder,
}, {
  path: '/timetable',
  title: 'TIMETABLE',
  subMenu: [],
  component: Timetable,
}, {
  path: '/about',
  title: 'ABOUT',
  subMenu: [],
  component: About,
}];
