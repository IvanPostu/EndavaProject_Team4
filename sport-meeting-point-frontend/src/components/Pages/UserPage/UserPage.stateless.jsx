import React from 'react'
import PropTypes from 'prop-types'
import { Container } from '../../Layouts/Container'
import style from './style.scss'
import unauthUserImg from '../../../../static/unauth_usr.png'



const PersonalDataLine = ({ title, text }) => (
  <p className={style.txt}>
    <span className={style.txt} style={{ fontWeight: 'bold' }}>
      {title}
    </span>
    {text}
  </p>
)


const UserPageStateless = ({ signOut, firstName, lastName, email, role }) => {


  return (
    <Container >
      <main className={style.main}>


        <div className={style.item}>
          <div className={style.miniItem}>
            <img id={style.userImg} src={unauthUserImg} alt="" />
          </div>
          <div className={style.miniItem}>
            <button className={`${style.btn} ${style.simpleBtn}`}>Load user image</button>
            <button className={`${style.btn} ${style.simpleBtn}`}>Reset personal data</button>
            <button onClick={() => signOut()} id={style.exitBtn} className={style.btn}>Exit</button>
          </div>
        </div>

        <div className={style.item}>
          <div style={{ alignItems: 'flex-start' }} className={style.miniItem}>
            <PersonalDataLine title='Email: ' text={email} />
            <PersonalDataLine title='First Name: ' text={firstName} />
            <PersonalDataLine title='Last Name: ' text={lastName} />
            <PersonalDataLine title='Authority: ' text={role.toLowerCase()} />

          </div>
          <div className={style.miniItem}></div>
          <div className={style.miniItem}></div>

        </div>

      </main>
    </Container >
  )
}

UserPageStateless.propTypes = {
  signOut: PropTypes.func
}

export default UserPageStateless