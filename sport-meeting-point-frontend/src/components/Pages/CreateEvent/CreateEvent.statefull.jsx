import React, { Component } from 'react'
import CreateEventStateless from './CreateEvent.stateless.jsx'
import { tokenWorker } from '../../../utils/token-worker'
import { adress } from '../../../utils/server-adress'
import axios from 'axios'

export default class CreateEventStatefull extends Component {

  constructor(props) {
    super(props)
    this.handleAllInputData.bind(this)
  }

  handleAllInputData(data) {

    const token = tokenWorker.loadTokenFromLocalStorage()
    // console.log(token)

    var formData = new FormData();
    formData.append("file", data.image)

    const newData = {
      title: data.title,
      address: data.address,
      previewMessage: data.previewMessage,
      description: data.previewMessage,
    }

    formData.append("data", JSON.stringify(newData))

    // formData.append("data", new Blob([JSON.stringify(newData)], {
    //   type: "application/json"
    // }))

    const headers = {
      'Content-Type': undefined,
      'Authorization': `Bearer_${token}`
    }

    axios.post(adress + '/api/event/add', formData, {
      headers: headers
    })
      .then((response) => {
        console.log(response)
      })
      .catch((error) => {
        console.log(error)
      })

  }



  render() {
    return <CreateEventStateless
      handleAllInputData={this.handleAllInputData}
    />
  }
}
