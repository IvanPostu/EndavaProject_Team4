import React from 'react'
import style from './style.scss'
import { Link } from 'react-router-dom'
import { LoadingType1 } from '../../../../Layouts/Loading'
import { FaUser, FaCalendarCheck, FaMapMarkedAlt, FaRunning } from 'react-icons/fa'
import { IconContext } from 'react-icons'
import { eventInfoUrl } from '../../../../App/AppConstRoutes'
import PropTypes from 'prop-types'
import * as categoryDict from 'data/SportCategories/SportCategories.dictionary'

const Element = ({ children, color = '#818181', size = '13px' }) => (
  <IconContext.Provider value={{ color: color, size: size }}>
    <span style={{ margin: '5px 5px 5px 15px' }}>
      {children}
    </span>
  </IconContext.Provider>
)

Element.propTypes = {
  children: PropTypes.element,
  color: PropTypes.string,
  size: PropTypes.element
}

function imgShower(img) {
  switch (img) {
    case null:
      return <LoadingType1 />
    default:
      return <img src={img} className={style.imgStyle} alt='' />
  }
}

export default function EventStateless({
  id,
  title,
  text,
  image,
  authorName,
  address,
  eventDate,
  category
}) {
  return (
    <div className={style.mainContainer}>
      <div className={style.mainContainerChild}>
        {imgShower(image)}
      </div>
      <div
        className={style.mainContainerChild}
      >
        <div className={style.mainContainerChildBox}>
          <p className={style.title}>{title}</p>
          <p className={style.textInfo}>

            <Element>
              <FaUser />
            </Element>

            {authorName}

            <Element>
              <FaCalendarCheck />
            </Element>

            {eventDate}

            <Element>
              <FaMapMarkedAlt />
            </Element>

            {address}

            <Element>
              <FaRunning />
            </Element>

            {categoryDict.keyToValue(category).eng}

          </p>

          <hr className={style.hrProp} />

          <p className={style.text}>
            {text}
          </p>

          <div className={style.linkProp}>
            <Link to={`${eventInfoUrl}?id=${id}`} style={{ color: '#15AD72' }}>
              Read More »
            </Link>
          </div>

        </div>
      </div>
    </div>
  )
}

EventStateless.propTypes = {
  id: PropTypes.number.isRequired,
  title: PropTypes.string,
  text: PropTypes.string,
  image: PropTypes.string,
  authorName: PropTypes.string,
  address: PropTypes.string,
  eventDate: PropTypes.string,
  category: PropTypes.string

}
