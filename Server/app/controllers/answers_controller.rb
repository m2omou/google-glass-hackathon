class AnswersController < ApplicationController
  # GET /answers
  # GET /answers.json
  include ActionController::MimeResponds

  def index
    @answers = Answer.all

    respond_to do |format|
      format.html
      format.json { render json: @answers }
    end
  end


  # GET /answers/1
  # GET /answers/1.json
  def show
    @answer = Answer.find(params[:id])

    render json: @answer
  end

  # POST /answers
  # POST /answers.json
  def create
    @answer = Answer.new(params[:answer])

    if @answer.save
      render json: @answer, status: :created, location: @answer
    else
      render json: @answer.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /answers/1
  # PATCH/PUT /answers/1.json
  def update
    @answer = Answer.find(params[:id])

    if @answer.update(params[:answer])
      head :no_content
    else
      render json: @answer.errors, status: :unprocessable_entity
    end
  end

  # DELETE /answers/1
  # DELETE /answers/1.json
  def destroy
    @answer = Answer.find(params[:id])
    @answer.destroy

    head :no_content
  end

  private


end
