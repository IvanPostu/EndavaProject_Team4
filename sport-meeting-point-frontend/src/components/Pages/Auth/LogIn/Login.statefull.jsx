import React, { Component } from 'react'
import LoginStateless from './Login.stateless.jsx'
import { connect } from 'react-redux'
import { Redirect } from 'react-router-dom'
import { index } from '../../../App/AppConstRoutes'
import PropTypes from 'prop-types'
import { FullPageLoading1 as Loading } from '../../../Layouts/Loading'
import { bindActionCreators } from 'redux'
import { loginUser } from '../../../../redux/actions/Authentication.actions'

class LoginStatefull extends Component {
  render () {
    if (this.props.isAuthenticated) return <Redirect to={index} />

    return (
      <div>
        {this.props.isAuthProccess && <Loading />}

        <LoginStateless
          onHandleBtnLogIn={(login, password) => { this.props.loginUser(login, password) }}
          errorMsg={this.props.statusErrorMessages}
        />

      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    isAuthenticated: state.authenticationData.isAuthenticated,
    isAuthProccess: state.authenticationData.isAuthProccess,
    statusErrorMessages: state.authenticationData.statusErrorMessages
  }
}

const mapDispatchToProps = dispatch => {
  return {
    loginUser: bindActionCreators(loginUser, dispatch)
  }
}

LoginStatefull.propTypes = {
  loginUser: PropTypes.func,
  statusErrorMessages: PropTypes.array,
  isAuthProccess: PropTypes.bool,
  isAuthenticated: PropTypes.bool
}

export default connect(mapStateToProps, mapDispatchToProps)(LoginStatefull)
