import React, { Component } from 'react'
import PropTypes from 'prop-types'
import NewsStateless from './News.stateless.jsx'
import style from './style.scss'
import { ButtonB } from '../../../../Layouts/Button'
import { Container } from '../../../../Layouts/Container'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { loadFixedNumberOfNews } from '../../../../../redux/actions/News.actions'

class NewsStatefull extends Component {
  constructor(props) {
    super(props)

  }



  componentDidMount() {

  }

  shouldComponentUpdate(nextProps, nextState) {
    return true
  }

  render() {
    return (
      <>
        <div id={style.mainContainer}>
          {
            this.props.allNews &&
            this.props.allNews.length > 0 &&
            this.props.allNews.map((item, index) => (
              <NewsStateless
                key={item.id}
                id={item.id}
                title={item.title}
                text={item.context}
                img={item.image}
                category={item.sportCategory}
              />
            ))

          }
        </div>

      </>
    )
  }
}

const mapStateToProps = state => ({
  allNews: state.allNewsData.allNews
})

const mapDispatchToProps = dispatch => {
  return {
    loadFixedNumberOfNews: bindActionCreators(loadFixedNumberOfNews, dispatch)
  }
}

NewsStatefull.propTypes = {
  allNews: PropTypes.array,
  loadFixedNumberOfNews: PropTypes.func
}

export default connect(mapStateToProps,
  mapDispatchToProps)(NewsStatefull)
