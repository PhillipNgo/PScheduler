import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import Generator from '../../components/content/Generator';

const mapStateToProps = state => ({
  isGenerating: state.generator.isGenerating,
});

const mapDispatchToProps = () => ({
});

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Generator));
