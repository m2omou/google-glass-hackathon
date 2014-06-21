class ScoresController < ApplicationController
  include ActionController::MimeResponds

  # GET /scores
  # GET /scores.json
  def index
    @scores = Score.all

    respond_to do |format|
      format.html
      format.json { render json: @scores }
    end
  end

  # GET /scores/1
  # GET /scores/1.json
  def show
    @score = Score.find(params[:id])
    render json: @score
  end

  # POST /scores
  # POST /scores.json
  def create
    @score = Score.new(score_params)

    if @score.save
      render json: @score, status: :created, location: @score
    else
      render json: @score.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /scores/1
  # PATCH/PUT /scores/1.json
  def update
    @score = Score.find(params[:id])

    if @score.update(params[:score])
      head :no_content
    else
      render json: @score.errors, status: :unprocessable_entity
    end
  end

  # DELETE /scores/1
  # DELETE /scores/1.json
  def destroy
    @score = Score.find(params[:id])
    @score.destroy

    head :no_content
  end

  private

  def score_params
    params.require(:score).permit(:answer_id, :target_id)
  end
end
