import React from 'react'
import style from '../style.scss'
import { Link } from 'react-router-dom'
import { registrationUrl } from '../../../../components/App/AppConstRoutes'
import { Container } from '../../../../components/Layouts/Container'
import PropTypes from 'prop-types'

export default function LoginStateless ({ onHandleBtnLogIn, errorMsg }) {
  const [loginData, setLoginData] = React.useState({
    username: '',
    password: ''
  })

  // console.log(errorMsg)

  return (
    <>
      <Container>
        <div className={style.card}>
          <div className={style.loginOrRegistrationBtn}>
            Log In /
            <Link to={registrationUrl}> Registration</Link>
          </div>
          <p>
            Email:
          </p>
          <input
            className={style.inputType1} type='text'
            onChange={e => setLoginData({ ...loginData, username: e.target.value })}
            value={loginData.username}
          />
          <br />
          <br />
          <p>Password:</p>
          <input
            className={style.inputType1} type='password'
            onChange={e => setLoginData({ ...loginData, password: e.target.value })}
            value={loginData.password}
          />
          <br /><br />

          <button
            className={style.btn1} onClick={() => {
              onHandleBtnLogIn(loginData.username, loginData.password)
              setLoginData({ ...loginData, password: '', username: '' })
            }}
          >
            Log In
          </button>

          <br />
          <br />

          {
            errorMsg.map((item, index) => <p key={index} className={style.errorMsg}>{item}</p>)
          }

        </div>

      </Container>
    </>
  )
}

LoginStateless.propTypes = {
  onHandleBtnLogIn: PropTypes.func,
  errorMsg: PropTypes.array
}
